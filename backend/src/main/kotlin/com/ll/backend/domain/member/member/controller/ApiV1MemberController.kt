package com.ll.backend.domain.member.member.controller

import com.ll.backend.domain.member.member.dto.MemberDto
import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.rq.Rq
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.base.Empty
import com.ll.backend.standard.extensions.getOrThrow
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/members")
class ApiV1MemberController(
    private val memberService: MemberService,
    private val rq: Rq
) {
    val currentActor
        get() = rq.actor


    data class MemberJoinReqBody(
        @field:NotBlank
        @field:NotNull
        val username: String,

        @field:NotBlank
        @field:NotNull
        val password: String,

        @field:NotBlank
        @field:NotNull
        val nickname: String
    )

    @PostMapping("/join")
    @Transactional
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
        @field:NotNull
        val username: String,
        @field:NotBlank
        @field:NotNull
        val password: String,
    )

    data class MemberLoginResBody(
        val item: MemberDto,
        val accessToken: String,
        val refreshToken: String,
    )

    @PostMapping("/login")
    @Transactional
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
    fun logout(req: HttpServletRequest): RsData<Empty> {
        rq.removeAuthCookies()
        return RsData("200-1", "로그아웃 되었습니다.")
    }


    @GetMapping("/me")
    @Transactional(readOnly = true)
    fun me(): RsData<MemberDto> {
        val member = memberService.findById(currentActor.id).getOrThrow()
        return RsData("200-1", "OK", MemberDto(member))
    }
}