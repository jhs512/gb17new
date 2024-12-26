package com.ll.backend.global.rq

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.security.SecurityUser
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class Rq(
    val req: HttpServletRequest,
    val res: HttpServletResponse
) {
    val isLogin: Boolean by lazy {
        SecurityContextHolder.getContext()?.authentication?.isAuthenticated ?: false
    }

    val user: SecurityUser by lazy {
        SecurityContextHolder.getContext()?.authentication?.principal as? SecurityUser
            ?: throw ServiceException("403-1", "securityUser 객체 취득실패, 로그인이 필요합니다.")
    }

    val actor: Member by lazy {
        val member = Member(
            user.id,
            user.createDate,
            user.modifyDate,
            user.username
        )

        return@lazy member
    }

    fun setLogin(securityUser: SecurityUser) {
        SecurityContextHolder.getContext().authentication = securityUser.genAuthentication()
    }

    fun makeAuthCookies(accessToken: String, refreshToken: String) {
        setCrossDomainCookie("accessToken", accessToken)
        setCrossDomainCookie("refreshToken", refreshToken)
    }

    fun removeAuthCookies() {
        removeCrossDomainCookie("accessToken")
        removeCrossDomainCookie("refreshToken")
    }

    private fun getSiteCookieDomain(): String? {
        var cookieDomain = AppConfig.siteCookieDomain

        if (cookieDomain != "localhost") {
            return ".$cookieDomain"
        }

        return null
    }

    fun setCrossDomainCookie(name: String, value: String) {
        val cookie = ResponseCookie.from(name, value)
            .path("/")
            .domain(getSiteCookieDomain())
            .sameSite("Strict")
            .secure(true)
            .httpOnly(true)
            .build()

        res.addHeader("Set-Cookie", cookie.toString())
    }

    fun removeCrossDomainCookie(name: String) {
        val cookie = ResponseCookie.from(name, "")
            .path("/")
            .maxAge(0)
            .domain(getSiteCookieDomain())
            .secure(true)
            .httpOnly(true)
            .build()

        res.addHeader("Set-Cookie", cookie.toString())
    }

    fun getCookie(name: String): Cookie? {
        return req.cookies?.find { it.name == name }
    }

    fun getCookieValue(name: String, defaultValue: String?): String? {
        val cookie = getCookie(name) ?: return defaultValue

        return cookie.value
    }
}
