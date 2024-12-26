package com.ll.backend.domain.member.member.controller

import com.ll.backend.domain.member.member.dto.MemberDto
import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.rq.Rq
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.base.Empty
import com.ll.backend.standard.extensions.getOrThrow
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/members")
@Transactional
@Tag(name = "ApiV1MemberController", description = "회원 API 컨트롤러")
class ApiV1MemberController(
    private val memberService: MemberService,
    private val rq: Rq
) {
    val currentActor
        get() = rq.actor


    data class MemberJoinReqBody(
        @field:NotBlank
        val username: String,
        @field:NotBlank
        val password: String,
        @field:NotBlank
        val nickname: String
    )

    @PostMapping("/join")
    @Transactional
    @Operation(summary = "회원가입")
    fun join(
        @RequestBody @Valid reqBody: MemberJoinReqBody
    ): RsData<MemberDto> {
        val member = memberService.join(reqBody.username, reqBody.password, reqBody.nickname);

        return RsData(
            "201-1",
            "${member.name}님 환영합니다.",
            MemberDto(member)
        )
    }


    data class MemberLoginReqBody(
        @field:NotBlank
        val username: String,
        @field:NotBlank
        val password: String,
    )

    data class MemberLoginResBody(
        val item: MemberDto,
        val accessToken: String,
        val refreshToken: String,
    )

    @PostMapping("/login")
    @Transactional
    @Operation(summary = "로그인")
    fun login(
        @RequestBody @Valid reqBody: MemberLoginReqBody
    ): RsData<MemberLoginResBody> {
        val member = memberService
            .findByUsername(reqBody.username)
            ?: throw ServiceException("401-1", "해당 회원은 존재하지 않습니다.")

        memberService.checkPasswordValidation(reqBody.password, member.password)

        val accessToken = memberService.genAccessToken(member)
        val refreshToken = member.refreshToken

        rq.makeAuthCookies(accessToken, refreshToken)

        return RsData(
            "201-1",
            "${member.name}님 환영합니다.",
            MemberLoginResBody(
                item = MemberDto(member),
                accessToken = memberService.genAccessToken(member),
                refreshToken = member.refreshToken
            )
        )
    }


    @DeleteMapping("/logout")
    @Transactional
    @Operation(summary = "로그아웃")
    fun logout(req: HttpServletRequest): RsData<Empty> {
        rq.removeAuthCookies()
        return RsData("200-1", "로그아웃 되었습니다.")
    }


    @GetMapping("/me")
    @Transactional(readOnly = true)
    @Operation(summary = "내 정보")
    fun me(): RsData<MemberDto> {
        val member = memberService.findById(currentActor.id).getOrThrow()

        return RsData("200-1", "OK", MemberDto(member))
    }
}