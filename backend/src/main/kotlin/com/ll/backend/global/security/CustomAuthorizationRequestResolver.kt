package com.ll.backend.global.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component

@Component
class CustomAuthorizationRequestResolver(
    clientRegistrationRepository: ClientRegistrationRepository
) : OAuth2AuthorizationRequestResolver {

    private val defaultResolver = DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization")

    override fun resolve(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
        val authorizationRequest = defaultResolver.resolve(request)
        return customizeAuthorizationRequest(authorizationRequest, request)
    }

    override fun resolve(request: HttpServletRequest?, clientRegistrationId: String?): OAuth2AuthorizationRequest? {
        val authorizationRequest = defaultResolver.resolve(request, clientRegistrationId)
        return customizeAuthorizationRequest(authorizationRequest, request)
    }

    private fun customizeAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest?
    ): OAuth2AuthorizationRequest? {
        if (authorizationRequest == null || request == null) {
            return null
        }

        // state 파라미터를 요청에서 가져옴
        val customState = request.getParameter("state")

        // 기존 요청의 추가 파라미터에 state 값 추가
        val additionalParameters = authorizationRequest.additionalParameters.toMutableMap()
        if (!customState.isNullOrEmpty()) {
            additionalParameters["state"] = customState
        }

        // 새로운 OAuth2AuthorizationRequest 생성
        return OAuth2AuthorizationRequest.from(authorizationRequest)
            .additionalParameters(additionalParameters)
            .state(customState) // state 값 설정
            .build()
    }
}