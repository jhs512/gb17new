package com.ll.backend.domain.member.member.controller

import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.util.Ut
import jakarta.servlet.http.Cookie
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiV1MemberControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val memberService: MemberService
) {
    companion object {
        // 샘플 데이터 생성기를 통해서 만들어진 회원의 수
        private const val MEMBER_TOTAL_ITEMS = 4
    }

    private fun bodyToRsData(resultActions: ResultActions): RsData<Map<String, *>> {
        val contentAsString = resultActions.andReturn().response.contentAsString
        return Ut.json.toObj(contentAsString)
    }

    @Test
    @DisplayName("POST /api/v1/members/join")
    fun t01() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "newuser",
                            "password": "1234",
                            "nickname": "new유저"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        val rsData = bodyToRsData(resultActions)
        val newPostId = rsData.data["id"] as Int
        assertThat(newPostId).isGreaterThan(MEMBER_TOTAL_ITEMS)

        // THEN
        resultActions
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.resultCode").value("201-1"))
            .andExpect(jsonPath("$.msg").value("new유저님 환영합니다."))
            .andExpect(jsonPath("$.data.id").value(newPostId))
            .andExpect(jsonPath("$.data.createDate").exists())
            .andExpect(jsonPath("$.data.modifyDate").exists())
            .andExpect(jsonPath("$.data.name").value("new유저"))
            .andExpect(jsonPath("$.data.nickname").value("new유저"))
    }


    @Test
    @DisplayName("POST /api/v1/members/join conflict")
    fun t02() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user1",
                            "password": "1234",
                            "nickname": "new유저"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.resultCode").value("409-1"))
            .andExpect(jsonPath("$.msg").value("이미 존재하는 아이디입니다."))
    }

    @Test
    @DisplayName("POST /api/v1/members/login")
    fun t03() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user1",
                            "password": "1234"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        val memberUser = memberService.findByUsername("user1").getOrThrow()

        // THEN
        resultActions
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.resultCode").value("201-1"))
            .andExpect(jsonPath("$.msg").value("${memberUser.name}님 환영합니다."))
            .andExpect(jsonPath("$.data.item.id").value(memberUser.id))
            .andExpect(jsonPath("$.data.item.createDate").exists())
            .andExpect(jsonPath("$.data.item.modifyDate").exists())
            .andExpect(jsonPath("$.data.item.name").value(memberUser.name))
            .andExpect(jsonPath("$.data.item.nickname").value(memberUser.name))
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").value(memberUser.refreshToken))
    }

    @Test
    @DisplayName("POST /api/v1/members/login, with wrong username, 401")
    fun t04() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user0",
                            "password": "1234"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.resultCode").value("401-1"))
            .andExpect(jsonPath("$.msg").value("해당 회원은 존재하지 않습니다."))
    }

    @Test
    @DisplayName("POST /api/v1/members/login, with wrong password, 401")
    fun t05() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user1",
                            "password": "12345"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.resultCode").value("401-1"))
            .andExpect(jsonPath("$.msg").value("비밀번호가 일치하지 않습니다."))
    }

    @Test
    @DisplayName("POST /api/v1/members/login, with no input, 400")
    fun t06() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.resultCode").value("400-1"))
            .andExpect(jsonPath("$.msg").value("요청 데이터가 올바르지 않습니다."))
    }

    @Test
    @DisplayName("POST /api/v1/members/login, with no password input, 400")
    fun t07() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user1",
                            "password": ""
                        }
                    """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.resultCode").value("400-1"))
            .andExpect(
                jsonPath("$.msg").value(
                    """
                password-NotBlank-must not be blank
            """.trimIndent()
                )
            )
    }

    @Test
    @DisplayName("POST /api/v1/members/login, with blank input, 400")
    fun t08() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "",
                            "password": ""
                        }
                    """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.resultCode").value("400-1"))
            .andExpect(
                jsonPath("$.msg").value(
                    """
                password-NotBlank-must not be blank
                username-NotBlank-must not be blank
            """.trimIndent()
                )
            )
    }

    @Test
    @DisplayName("POST /api/v1/members/login, make cross domain cookies")
    fun t09() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user1",
                            "password": "1234"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        val rsData = bodyToRsData(resultActions)
        val accessToken = rsData.data["accessToken"] as String
        val refreshToken = rsData.data["refreshToken"] as String

        // THEN
        resultActions
            .andExpect(status().is2xxSuccessful)
            .andExpect { result ->
                val accessTokenCookie = result.response.getCookie("accessToken")!!

                assertThat(accessTokenCookie.value).isEqualTo(accessToken)
                assertThat(accessTokenCookie.path).isEqualTo("/")
                assertThat(accessTokenCookie.isHttpOnly).isTrue()
                assertThat(accessTokenCookie.secure).isTrue()

                val refreshTokenCookie = result.response.getCookie("refreshToken")!!
                assertThat(refreshTokenCookie.value).isEqualTo(refreshToken)
                assertThat(refreshTokenCookie.path).isEqualTo("/")
                assertThat(refreshTokenCookie.isHttpOnly).isTrue()
                assertThat(refreshTokenCookie.secure).isTrue()
            }
    }

    @Test
    @DisplayName("DELETE /api/v1/members/logout")
    fun t10() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                delete("/api/v1/members/logout")
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("로그아웃 되었습니다."))
            .andExpect { result ->
                assertThat(result.response.getCookie("accessToken")!!.maxAge).isEqualTo(0)
                assertThat(result.response.getCookie("refreshToken")!!.maxAge).isEqualTo(0)
            }
    }

    @Test
    @WithUserDetails("user1")
    @DisplayName("GET /api/v1/members/me")
    fun t11() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/members/me")
            )
            .andDo(print())

        val memberUser = memberService.findByUsername("user1").getOrThrow()

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("OK"))
            .andExpect(jsonPath("$.data.id").value(memberUser.id))
            .andExpect(jsonPath("$.data.createDate").exists())
            .andExpect(jsonPath("$.data.modifyDate").exists())
            .andExpect(jsonPath("$.data.name").value(memberUser.name))
            .andExpect(jsonPath("$.data.nickname").value(memberUser.name))
    }

    @Test
    @DisplayName("GET /api/v1/members/me, with authorization header")
    fun t12() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/members/me")
                    .header("Authorization", "Bearer user1 EMPTY")
            )
            .andDo(print())

        val memberUser = memberService.findByUsername("user1").getOrThrow()

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("OK"))
            .andExpect(jsonPath("$.data.id").value(memberUser.id))
            .andExpect(jsonPath("$.data.createDate").exists())
            .andExpect(jsonPath("$.data.modifyDate").exists())
            .andExpect(jsonPath("$.data.name").value(memberUser.name))
            .andExpect(jsonPath("$.data.nickname").value(memberUser.name))
    }

    @Test
    @DisplayName("GET /api/v1/members/me, with authorization cookies")
    fun t13() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/members/me")
                    .cookie(
                        Cookie("refreshToken", "user1"),
                        Cookie("accessToken", "EMPTY"),
                    )
            )
            .andDo(print())

        val memberUser = memberService.findByUsername("user1").getOrThrow()

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("OK"))
            .andExpect(jsonPath("$.data.id").value(memberUser.id))
            .andExpect(jsonPath("$.data.createDate").exists())
            .andExpect(jsonPath("$.data.modifyDate").exists())
            .andExpect(jsonPath("$.data.name").value(memberUser.name))
            .andExpect(jsonPath("$.data.nickname").value(memberUser.name))
    }
}