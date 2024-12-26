package com.ll.backend.domain.member.member.repository

import com.ll.backend.domain.member.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUsername(username: String): Member?
    fun findByRefreshToken(refreshToken: String): Member?
}
