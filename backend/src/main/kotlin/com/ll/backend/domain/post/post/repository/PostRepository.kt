package com.ll.backend.domain.post.post.repository

import com.ll.backend.domain.post.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
    fun findByPublished(published: Boolean, pageable: PageRequest): Page<Post>
}
