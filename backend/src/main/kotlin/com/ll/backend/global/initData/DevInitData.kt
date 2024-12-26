package com.ll.backend.global.initData

import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.standard.util.Ut
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.Transactional


@Configuration
@Profile("dev")
class DevInitData(
    private val postService: PostService,
    private val memberService: MemberService
) {
    @Autowired
    @Lazy
    private lateinit var self: DevInitData

    @Bean
    fun initDataDevApplicationRunner(): ApplicationRunner {
        return ApplicationRunner {
            self.work1()
            self.work2()
        }
    }

    @Transactional
    fun work1() {
        val backUrl = AppConfig.siteBackUrl

        val downloadFilePath: String = Ut.file.downloadFileByHttp("$backUrl/v3/api-docs/apiV1", ".")
        Ut.file.moveFile(downloadFilePath, "apiV1.json")

        val sb = StringBuilder()
        sb.append("rm -f apiV1.json")
        sb.append(" && ")
        sb.append("npx --package typescript --package openapi-typescript openapi-typescript apiV1.json -o ../frontend/src/lib/backend/apiV1/schema.d.ts")
        sb.append(" && ")
        sb.append("rm -f apiV1.json")

        Ut.cmd.runAsync(sb.toString())
    }

    @Transactional
    fun work2() {
    }
}