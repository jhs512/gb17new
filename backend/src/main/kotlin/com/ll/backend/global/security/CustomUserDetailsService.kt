package com.ll.backend.global.security

import com.ll.backend.domain.member.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("해당 유저를 찾을 수 없습니다. username: $username")

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