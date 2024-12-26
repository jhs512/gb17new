package com.ll.backend.global.app

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AppConfig(
    @Value("\${custom.jwt.secretKey}") private val _jwtSecretKey: String,
    @Value("\${custom.accessToken.expirationSec}") private val _accessTokenExpirationSec: Long,
    @Value("\${custom.site.frontUrl}") private val _siteFrontUrl: String,
    @Value("\${custom.site.backUrl}") private val _siteBackUrl: String,
    @Value("\${custom.site.cookieDomain}") private val _siteCookieDomain: String,
    @Value("\${custom.temp.dirPath}") private val _tempDirPath: String,
    @Value("\${custom.genFile.dirPath}") private val _genFileDirPath: String,
    @Value("\${custom.site.name}") private val _siteName: String,
    private val _objectMapper: ObjectMapper
) {
    companion object {
        lateinit var jwtSecretKey: String
        var accessTokenExpirationSec: Long = 0
        lateinit var siteFrontUrl: String
        lateinit var siteBackUrl: String
        lateinit var siteCookieDomain: String
        lateinit var tempDirPath: String
        lateinit var genFileDirPath: String
        lateinit var siteName: String
        lateinit var objectMapper: ObjectMapper
        const val basePageSize = 30

        fun isNotProd(): Boolean {
            return true
        }
    }

    @PostConstruct
    fun initStaticFields() {
        jwtSecretKey = _jwtSecretKey
        accessTokenExpirationSec = _accessTokenExpirationSec
        siteFrontUrl = _siteFrontUrl
        siteBackUrl = _siteBackUrl
        siteCookieDomain = _siteCookieDomain
        tempDirPath = _tempDirPath
        genFileDirPath = _genFileDirPath
        siteName = _siteName
        objectMapper = _objectMapper
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}