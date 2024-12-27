package com.ll.backend.global.security

import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.rq.Rq
import com.ll.backend.standard.extensions.getOrThrow
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationSuccessHandler(
    private val rq: Rq,
    private val memberService: MemberService
) : SavedRequestAwareAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val state = request?.getParameter("state")

        if (AppConfig.isFrontUrl(state)) {
            val member = memberService.findById(rq.actor.id).getOrThrow()

            rq.makeAuthCookies(member)

            response?.sendRedirect(state)
            return
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }
}