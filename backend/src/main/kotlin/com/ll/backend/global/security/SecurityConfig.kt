package com.ll.backend.global.security

import com.ll.backend.global.app.AppConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val customAuthenticationEntryPoint: AuthenticationEntryPoint,
    private val customAuthenticationSuccessHandler: CustomAuthenticationSuccessHandler
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity {
            authorizeHttpRequests {
                authorize(HttpMethod.GET, "/api/*/posts", permitAll)
                authorize(HttpMethod.GET, "/api/*/posts/{id}", permitAll)
                authorize(HttpMethod.POST, "/api/*/members/login", permitAll)
                authorize(HttpMethod.DELETE, "/api/*/members/logout", permitAll)
                authorize(HttpMethod.POST, "/api/*/members/join", permitAll)
                authorize("/api/v1/**", authenticated)
                authorize(anyRequest, permitAll)
            }

            headers {
                frameOptions {
                    sameOrigin = true
                }
            }

            csrf { disable() }

            formLogin { disable() }

            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            exceptionHandling {
                authenticationEntryPoint = customAuthenticationEntryPoint
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)

            oauth2Login {
                authenticationSuccessHandler = customAuthenticationSuccessHandler
            }
        }

        return httpSecurity.build()
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("https://cdpn.io", AppConfig.siteFrontUrl)
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/*/**", configuration)
        return source
    }
}