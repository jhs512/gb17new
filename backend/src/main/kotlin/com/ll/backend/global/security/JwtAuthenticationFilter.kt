package com.ll.backend.global.security

import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.rq.Rq
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val rq: Rq,
    private val memberService: MemberService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!request.requestURI.startsWith("/api/") || isExcludedPath(request.requestURI)) {
            filterChain.doFilter(request, response)
            return
        }

        val bearerToken = rq.req.getHeader("Authorization") ?: ""
        if (bearerToken.startsWith("Bearer ")) {
            processBearerToken(bearerToken)
        } else {
            processCookieTokens()
        }

        filterChain.doFilter(request, response)
    }

    private fun isExcludedPath(requestURI: String): Boolean {
        val excludedPaths = listOf(
            "/api/v1/members/login",
            "/api/v1/members/join",
            "/api/v1/members/logout"
        )
        return requestURI in excludedPaths
    }

    private fun isEmptyAccessToken(accessToken: String): Boolean {
        return accessToken.isBlank() || accessToken == "EMPTY"
    }

    private fun processBearerToken(bearerToken: String) {
        val tokens = bearerToken.removePrefix("Bearer ").split(" ", limit = 2)
        val refreshToken = tokens.firstOrNull() ?: ""
        var accessToken = tokens.getOrNull(1) ?: ""

        if (!isEmptyAccessToken(accessToken)) {
            accessToken = refreshAccessTokenIfNeeded(accessToken, refreshToken)
            val securityUser = memberService.getSecurityUserFromAccessToken(accessToken)
            rq.setLogin(securityUser)
        } else if (refreshToken.isNotBlank()) {
            authenticateWithRefreshToken(refreshToken)
        }
    }

    private fun processCookieTokens() {
        val accessToken = rq.getCookieValue("accessToken", "") ?: ""

        if (!isEmptyAccessToken(accessToken)) {
            val refreshToken = rq.getCookieValue("refreshToken", "") ?: ""
            val validAccessToken = refreshAccessTokenIfNeeded(accessToken, refreshToken)
            val securityUser = memberService.getSecurityUserFromAccessToken(validAccessToken)
            rq.setLogin(securityUser)
        } else {
            val refreshToken = rq.getCookieValue("refreshToken", "") ?: ""
            if (refreshToken.isNotBlank()) {
                authenticateWithRefreshToken(refreshToken)
            }
        }
    }

    private fun refreshAccessTokenIfNeeded(accessToken: String, refreshToken: String): String {
        return if (!memberService.validateToken(accessToken)) {
            val rs = memberService.refreshAccessToken(refreshToken)
            rq.setCrossDomainCookie("accessToken", rs.data)
            rs.data
        } else {
            accessToken
        }
    }

    private fun authenticateWithRefreshToken(refreshToken: String) {
        memberService.genSecurityUserByRefreshToken(refreshToken)?.let {
            rq.setLogin(it)
        }
    }
}
