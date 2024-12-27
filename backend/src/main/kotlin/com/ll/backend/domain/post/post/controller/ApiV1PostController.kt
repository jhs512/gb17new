package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.author.entity.Author
import com.ll.backend.domain.post.post.dto.PostDto
import com.ll.backend.domain.post.post.dto.PostWithBodyDto
import com.ll.backend.domain.post.post.entity.Post
import com.ll.backend.domain.post.post.repository.PostRepository
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.rq.Rq
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.base.Empty
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.page.dto.PageDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "ApiV1PostController", description = "글 API 컨트롤러")
class ApiV1PostController(
    private val rq: Rq,
    private val postService: PostService,
    private val postRepository: PostRepository
) {
    val currentActor
        get() = Author(rq.actor)


    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "공개글 다건조회")
    fun getItems(
        page: Int = 1,
        @Min(1) @Max(50) pageSize: Int = AppConfig.basePageSize,
        searchKeyword: String = ""
    ): PageDto<PostDto> {
        return PageDto(
            postService
                .findByPublishedAndSearchKeywordPaged(
                    true,
                    searchKeyword,
                    page,
                    pageSize
                )
                .map { PostDto(it) }
        )
    }


    @GetMapping("/mine")
    @Transactional(readOnly = true)
    @Operation(summary = "내글 다건조회")
    fun getMine(
        page: Int = 1,
        @Min(1) @Max(50) pageSize: Int = AppConfig.basePageSize,
        searchKeyword: String = ""
    ): PageDto<PostDto> {
        return PageDto(
            postService
                .findByAuthorAndSearchKeywordPaged(
                    currentActor,
                    searchKeyword,
                    page,
                    pageSize
                )
                .map { PostDto(it) }
        )
    }


    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "단건조회")
    fun getItem(
        @PathVariable id: Long
    ): PostWithBodyDto {
        val post = postService.findById(id)
            .getOrThrow()

        if (!post.published && !rq.isLogin) {
            throw ServiceException("403-1", "비공개글은 작성자만 조회할 수 있습니다.")
        }

        if (!post.published)
            postService.checkPermissionToRead(currentActor, post)

        return PostWithBodyDto(post)
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
    @Operation(summary = "글 작성")
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


    @PostMapping("/temp")
    @Transactional
    @Operation(summary = "임시글 생성")
    fun makeTemp(): RsData<PostDto> {
        postService.checkPermissionToWrite(currentActor)

        findTemp(currentActor)
            ?.let {
                return RsData(
                    "200-1",
                    "${it.id}번 임시글을 불러옵니다.",
                    PostDto(it)
                )
            }

        val post = postService.makeTemp(currentActor)

        return RsData(
            "201-1",
            "${post.id}번 임시글이 생성되었습니다.",
            PostDto(post)
        )
    }

    private fun findTemp(author: Author): Post? {
        return postRepository.findByAuthorAndPublishedAndTitle(author, false, "임시글")
    }


    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "글 삭제")
    fun delete(@PathVariable id: Long): RsData<Empty> {
        val post = postService
            .findById(id)
            .getOrThrow()

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
    @Operation(summary = "글 수정")
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