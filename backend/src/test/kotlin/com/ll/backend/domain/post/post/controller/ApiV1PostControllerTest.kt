package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.post.entity.Post
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.util.Ut
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiV1PostControllerTest @Autowired constructor(
    private val postService: PostService,
    private val mockMvc: MockMvc
) {
    private fun bodyToRsData(resultActions: ResultActions): RsData<Map<String, *>> {
        val contentAsString = resultActions.andReturn().response.contentAsString
        return Ut.json.toObj(contentAsString)
    }

    @Test
    @DisplayName("GET /api/v1/posts/1")
    fun t01() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("안녕하세요."))
            .andExpect(jsonPath("$.content").value("반갑습니다."))
            .andExpect(jsonPath("$.published").value(true))
    }

    @Test
    @DisplayName("GET /api/v1/posts/2")
    fun t02() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/posts/2")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.title").value("Hello."))
            .andExpect(jsonPath("$.content").value("Nice to meet you."))
            .andExpect(jsonPath("$.published").value(true))
    }

    @Test
    @DisplayName("GET /api/v1/posts")
    fun t03() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/posts")
            )
            .andDo(print())

        val postPage: Page<Post> = postService
            .findByPublishedPaged(true, 1, AppConfig.basePageSize)

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items.length()").value(postPage.numberOfElements))
            .andExpect(jsonPath("$.totalItems").value(postPage.totalElements))
            .andExpect(jsonPath("$.totalPages").value(postPage.totalPages))
            .andExpect(jsonPath("$.currentPageNumber").value(postPage.number + 1))
            .andExpect(jsonPath("$.pageSize").value(postPage.size))

        val posts = postPage.content

        for (i in posts.indices) {
            resultActions
                .andExpect(jsonPath("$.items[$i].id").value(posts[i].id))
                .andExpect(jsonPath("$.items[$i].title").value(posts[i].title))
                .andExpect(jsonPath("$.items[$i].content").doesNotExist())
                .andExpect(jsonPath("$.items[$i].published").value(posts[i].published))
        }
    }

    @Test
    @DisplayName("GET /api/v1/posts?page=2&pageSize=1")
    fun t04() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/posts")
                    .param("page", "2")
                    .param("pageSize", "1")
            )
            .andDo(print())

        val postPage = postService
            .findByPublishedPaged(true, 2, 1)

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items.length()").value(postPage.numberOfElements))
            .andExpect(jsonPath("$.totalItems").value(postPage.totalElements))
            .andExpect(jsonPath("$.totalPages").value(postPage.totalPages))
            .andExpect(jsonPath("$.currentPageNumber").value(postPage.number + 1))
            .andExpect(jsonPath("$.pageSize").value(postPage.size))

        val posts = postPage.content

        for (i in posts.indices) {
            resultActions
                .andExpect(jsonPath("$.items[$i].id").value(posts[i].id))
                .andExpect(jsonPath("$.items[$i].title").value(posts[i].title))
                .andExpect(jsonPath("$.items[$i].content").doesNotExist())
                .andExpect(jsonPath("$.items[$i].published").value(posts[i].published))
        }
    }

    @Test
    @DisplayName("POST /api/v1/posts, with user1, 200")
    @WithUserDetails("user1")
    fun t05() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "title": "글 제목",
                            "content": "글 내용",
                            "published": true
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        val rsData = bodyToRsData(resultActions)
        val newPostId = rsData.data["id"] as Int

        assertThat(newPostId).isGreaterThan(2)

        // THEN
        resultActions
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.resultCode").value("201-1"))
            .andExpect(jsonPath("$.msg").value("${newPostId}번 글이 작성되었습니다."))
            .andExpect(jsonPath("$.data.id").value(newPostId))
            .andExpect(jsonPath("$.data.title").value("글 제목"))
            .andExpect(jsonPath("$.data.content").doesNotExist())
            .andExpect(jsonPath("$.data.published").value(true))
    }

    @Test
    @DisplayName("POST /api/v1/posts, with user1, without content, 400")
    @WithUserDetails("user1")
    fun t07() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "title": "글 제목",
                            "content": "",
                            "published": true
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.resultCode").value("400-1"))
            .andExpect(jsonPath("$.msg").value("content-NotBlank-must not be blank"))
    }

    @Test
    @DisplayName("DELETE /api/v1/posts/1, with user1, 200")
    @WithUserDetails("user1")
    fun t08() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                delete("/api/v1/posts/1")
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("1번 글이 삭제되었습니다."))

        assertThat(postService.findById(1)).isNull()
    }

    @Test
    @DisplayName("PUT /api/v1/posts/1, with user1, 200")
    @WithUserDetails("user1")
    fun t09() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                put("/api/v1/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "title": "제목 수정",
                            "content": "내용 수정",
                            "published": true
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("1번 글이 수정되었습니다."))
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.title").value("제목 수정"))
            .andExpect(jsonPath("$.data.content").doesNotExist())
            .andExpect(jsonPath("$.data.published").value(true))

        val post = postService.findById(1).getOrThrow()
        assertThat(post.title).isEqualTo("제목 수정")
        assertThat(post.content).isEqualTo("내용 수정")
    }

    @Test
    @DisplayName("POST /api/v1/posts/1, without user, 401")
    fun t10() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "title": "글 제목",
                            "content": "글 내용",
                            "published": true
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.resultCode").value("401-1"))
            .andExpect(jsonPath("$.msg").value("로그인 후 이용해주세요."))
    }

    @Test
    @DisplayName("DELETE /api/v1/posts/1 with user2, 403")
    @WithUserDetails("user2")
    fun t11() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                delete("/api/v1/posts/1")
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.resultCode").value("403-1"))
            .andExpect(jsonPath("$.msg").value("글의 작성자만 삭제할 수 있습니다."))

        assertThat(postService.findById(1)).isNotNull
    }

    @Test
    @DisplayName("PUT /api/v1/posts/1 with user2, 403")
    @WithUserDetails("user2")
    fun t12() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                put("/api/v1/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "title": "제목 수정",
                            "content": "내용 수정",
                            "published": true
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.resultCode").value("403-1"))
            .andExpect(jsonPath("$.msg").value("글의 작성자만 수정할 수 있습니다."))
    }

    @Test
    @WithUserDetails("user1")
    @DisplayName("DELETE /api/v1/posts/2, no permission to delete, 403")
    fun t13() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                delete("/api/v1/posts/2")
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.resultCode").value("403-1"))
            .andExpect(jsonPath("$.msg").value("글의 작성자만 삭제할 수 있습니다."))
    }
}