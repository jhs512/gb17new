package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.author.entity.Author
import com.ll.backend.domain.post.post.dto.PostDto
import com.ll.backend.domain.post.post.dto.PostWithBodyDto
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.rq.Rq
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.base.Empty
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.page.dto.PageDto
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
@Validated
class ApiV1PostController(
    private val rq: Rq,
    private val postService: PostService
) {
    val currentActor
        get() = Author(rq.actor)

    @GetMapping
    @Transactional(readOnly = true)
    fun getItems(
        page: Int = 1,
        @Min(1) @Max(50) pageSize: Int = AppConfig.basePageSize
    ): PageDto<PostDto> {
        return PageDto(
            postService
                .findByPublishedPaged(true, page, pageSize)
                .map { PostDto(it) }
        )
    }


    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    fun getItem(
        @PathVariable id: Long
    ): PostWithBodyDto {
        return postService.findById(id)
            .getOrThrow()
            .let { PostWithBodyDto(it) }
    }


    data class PostWriteReqBody(
        @field:NotBlank
        val title: String,
        @field:NotBlank
        val content: String,
        val published: Boolean = false
    )

    @PostMapping
    @Transactional
    fun write(
        @RequestBody @Valid reqBody: PostWriteReqBody
    ): RsData<PostDto> {
        postService.checkPermissionToWrite(currentActor)

        val post = postService.write(currentActor, reqBody.title, reqBody.content, reqBody.published)

        return RsData(
            "201-1",
            "${post.id}번 글이 작성되었습니다.",
            PostDto(post)
        )
    }


    @DeleteMapping("/{id}")
    @Transactional
    fun delete(@PathVariable id: Long): RsData<Empty> {
        val post = postService.findById(id).getOrThrow()

        postService.checkPermissionToDelete(currentActor, post)

        postService.delete(post)

        return RsData("200-1", "${id}번 글이 삭제되었습니다.")
    }


    data class PostModifyReqBody(
        @field:NotBlank
        val title: String,
        @field:NotBlank
        val content: String,
        val published: Boolean = false
    )

    @PutMapping("/{id}")
    @Transactional
    fun modify(
        @PathVariable id: Long,
        @RequestBody @Valid reqBody: PostModifyReqBody
    ): RsData<PostDto> {
        val post = postService.findById(id).getOrThrow()

        postService.checkPermissionToModify(currentActor, post)

        postService.modify(post, reqBody.title, reqBody.content, reqBody.published)

        return RsData(
            "200-1",
            "${id}번 글이 수정되었습니다.",
            PostDto(post)
        )
    }
}