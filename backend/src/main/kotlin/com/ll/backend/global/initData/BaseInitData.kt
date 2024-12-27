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
        AppConfig.isNotProd().let { memberSystem.refreshToken = "system-apikey" }

        val memberAdmin = memberService.join("admin", "1234", "관리자")
        AppConfig.isNotProd().let { memberAdmin.refreshToken = "admin-apikey" }

        val memberUser1 = memberService.join("user1", "1234", "유저1")
        AppConfig.isNotProd().let { memberUser1.refreshToken = "user1-apikey" }

        val memberUser2 = memberService.join("user2", "1234", "유저2")
        AppConfig.isNotProd().let { memberUser2.refreshToken = "user2-apikey" }

        val memberUser3 = memberService.join("user3", "1234", "유저3")
        AppConfig.isNotProd().let { memberUser2.refreshToken = "user3-apikey" }
    }

    @Transactional
    fun work2() {
        if (postService.count() > 0) return

        val memberUser1 = memberService.findByUsername("user1").getOrThrow()
        val memberUser2 = memberService.findByUsername("user2").getOrThrow()
        val memberUser3 = memberService.findByUsername("user3").getOrThrow()

        postService.write(Author(memberUser1), "안녕하세요.", "반갑습니다.", true)
        postService.write(Author(memberUser2), "Hello.", "Nice to meet you.", true)
        postService.write(
            Author(memberUser2), "반갑습니다.", """
            ${'$'}${'$'}chart
            ,category1,category2
            Jan,21,23
            Feb,31,17
            
            type: column
            title: Monthly Revenue
            x.title: Amount
            y.title: Month
            y.min: 1
            y.max: 40
            y.suffix: ${'$'}
            ${'$'}${'$'}
            ```js
            console.log('foo')
            ```
            ```javascript
            console.log('bar')
            ```
            ```html
            <div id="editor"><span>baz</span></div>
            ```
            ```wrong
            [1 2 3]
            ```
            ```clojure
            [1 2 3]
            ```
            | @cols=2:merged |
            | --- | --- |
            | table | table2 |
            ${'$'}${'$'}uml
            partition Conductor {
              (*) --> "Climbs on Platform"
              --> === S1 ===
              --> Bows
            }
            
            partition Audience #LightSkyBlue {
              === S1 === --> Applauds
            }
            
            partition Conductor {
              Bows --> === S2 ===
              --> WavesArmes
              Applauds --> === S2 ===
            }
            
            partition Orchestra #CCCCEE {
              WavesArmes --> Introduction
              --> "Play music"
            }
            ${'$'}${'$'}
            
            # 자바
            ## 자바입니다.
            
            ```java
            int a = 10;
            ```
            
            - [ ] 하하
            - [ ] 호호
            
            1. - [ ] [문제 - 배열에 과일이름들을 담아주세요, push 사용](https://codepen.io/jangka44/pen/abEppor?editors=0012)
                - [정답예시](https://codepen.io/jangka44/pen/ExoZNqJ?editors=0102)
                - [정답](https://codepen.io/jangka44/pen/ExoZNqJ?editors=0012)
            1. - [ ] [문제 - 배열에 과일이름들을 담아주세요, push 사용](https://codepen.io/jangka44/pen/abEppor?editors=0012)
        """.trimIndent(), true
        )

        postService.write(Author(memberUser3), "Hello.", "Nice to meet you.", false)
    }
}