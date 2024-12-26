package com.ll.backend.global.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.core.user.OAuth2User
import java.time.LocalDateTime


class SecurityUser : User, OAuth2User {
    constructor(
        id: Long,
        createDate: LocalDateTime,
        modifyDate: LocalDateTime,
        username: String,
        password: String,
        authorities: Collection<GrantedAuthority>
    ) : super(username, password, authorities) {
        this.id = id
        this.createDate = createDate
        this.modifyDate = modifyDate
    }

    val id: Long

    val createDate: LocalDateTime

    val modifyDate: LocalDateTime

    fun genAuthentication(): Authentication {
        return UsernamePasswordAuthenticationToken(
            this,
            this.password,
            this.authorities
        )
    }

    override fun getName(): String {
        return this.username
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return mutableMapOf()
    }
}