package com.ll.backend.domain.member.member.service

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.global.app.AppConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*

@Service
class AuthTokenService {
    private val secretKey by lazy {
        Keys.hmacShaKeyFor(AppConfig.jwtSecretKey.toByteArray())
    }

    fun genToken(member: Member, expireSeconds: Long): String {
        val claims = Jwts.claims()
            .add("id", member.id)
            .add("username", member.username)
            .add("authorities", member.authorities)
            .build()

        val issuedAt: Date = Date()
        val expiration: Date = Date(issuedAt.time + 1000 * expireSeconds)

        return Jwts.builder()
            .claims(claims)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(secretKey) // 새로운 방식으로 서명
            .compact()
    }

    fun genAccessToken(member: Member): String {
        return genToken(member, AppConfig.accessTokenExpirationSec)
    }

    fun getDataFrom(token: String): Map<String, *> {
        val payload: Claims = Jwts.parser()
            .verifyWith(secretKey) // 새로운 검증 방식
            .build()
            .parseSignedClaims(token) // parseClaimsJws 대신 사용
            .payload

        return mapOf(
            "id" to payload.get("id", Integer::class.java),
            "username" to payload.get("username", String::class.java),
            "authorities" to payload.get("authorities", List::class.java)
        )
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun genRefreshToken(): String {
        val random = SecureRandom()
        val bytes = ByteArray(10)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().encodeToString(bytes)
    }
}
