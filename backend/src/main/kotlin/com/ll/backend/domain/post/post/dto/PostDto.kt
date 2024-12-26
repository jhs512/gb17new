package com.ll.backend.domain.post.post.dto

import com.ll.backend.domain.post.post.entity.Post
import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val actorId: Long,
    val authorName: String,
    val title: String,
    val published: Boolean
) {
    constructor(post: Post) : this(
        id = post.id,
        createDate = post.createDate,
        modifyDate = post.modifyDate,
        actorId = post.author.id,
        authorName = post.author.name,
        title = post.title,
        published = post.published
    )
}
