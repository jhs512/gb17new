package com.ll.backend.domain.post.post.dto

import com.ll.backend.domain.post.post.entity.Post
import java.time.LocalDateTime

data class PostWithContentDto(
    val id: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val authorId: Long,
    val authorName: String,
    val title: String,
    val content: String,
    val published: Boolean
) {
    constructor(post: Post) : this(
        id = post.id,
        createDate = post.createDate,
        modifyDate = post.modifyDate,
        authorId = post.author.id,
        authorName = post.author.name,
        title = post.title,
        content = post.body.content,
        published = post.published
    )
}
