package com.ll.backend.global.security

import com.ll.backend.domain.member.member.service.MemberService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CustomOAuth2UserService(
    private val memberService: MemberService
) : DefaultOAuth2UserService() {
    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val oauthId = oAuth2User.name
        val providerTypeCode = userRequest
            .clientRegistration
            .registrationId
            .uppercase(Locale.getDefault())

        val attributes = oAuth2User.attributes
        val attributesProperties = attributes["properties"] as Map<String, String>

        val nickname = attributesProperties["nickname"]!!
        val profileImgUrl = attributesProperties["profile_image"]!!
        val username = "${providerTypeCode}__${oauthId}"
        val member = memberService.modifyOrJoin(username, nickname, profileImgUrl)

        return SecurityUser(
            member.id,
            member.createDate,
            member.modifyDate,
            member.username,
            member.password,
            member.authorities
        )
    }
}