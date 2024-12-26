package com.ll.backend.global.initData

import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.domain.post.author.entity.Author
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.standard.extensions.getOrThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.transaction.annotation.Transactional

@Configuration
class BaseInitData(
    private val postService: PostService,
    private val memberService: MemberService
) {
    @Autowired
    @Lazy
    private lateinit var self: BaseInitData

    @Bean
    fun initDataBaseApplicationRunner(): ApplicationRunner {
        return ApplicationRunner {
            self.work1()
            self.work2()
        }
    }

    @Transactional
    fun work1() {
        if (memberService.count() > 0) return

        val memberSystem = memberService.join("system", "1234", "시스템")
        AppConfig.isNotProd().let { memberSystem.refreshToken = "system" }

        val memberAdmin = memberService.join("admin", "1234", "관리자")
        AppConfig.isNotProd().let { memberAdmin.refreshToken = "admin" }

        val memberUser1 = memberService.join("user1", "1234", "유저1")
        AppConfig.isNotProd().let { memberUser1.refreshToken = "user1" }

        val memberUser2 = memberService.join("user2", "1234", "유저2")
        AppConfig.isNotProd().let { memberUser2.refreshToken = "user2" }
    }

    @Transactional
    fun work2() {
        if (postService.count() > 0) return

        val memberUser1 = memberService.findByUsername("user1").getOrThrow()
        val memberUser2 = memberService.findByUsername("user2").getOrThrow()

        postService.write(Author(memberUser1), "안녕하세요.", "반갑습니다.", true)
        postService.write(Author(memberUser2), "Hello.", "Nice to meet you.", true)
    }
}