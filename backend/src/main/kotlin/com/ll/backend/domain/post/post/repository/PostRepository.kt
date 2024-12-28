package com.ll.backend.domain.post.post.repository

import com.ll.backend.domain.post.author.entity.Author
import com.ll.backend.domain.post.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
    fun findByAuthor(
        author: Author,
        pageable: PageRequest
    ): Page<Post>

    fun findByAuthorAndBody_ContentLike(
        author: Author,
        searchKeyword: String,
        pageable: PageRequest
    ): Page<Post>

    fun findByAuthorAndTitleLike(
        author: Author,
        searchKeyword: String,
        pageable: PageRequest
    ): Page<Post>

    fun findByPublished(
        published: Boolean,
        pageable: PageRequest
    ): Page<Post>

    fun findByPublishedAndBody_ContentLike(
        published: Boolean,
        searchKeyword: String,
        pageable: PageRequest
    ): Page<Post>

    fun findByPublishedAndTitleLike(
        published: Boolean,
        searchKeyword: String,
        pageable: PageRequest
    ): Page<Post>

    fun findByAuthorAndPublishedAndTitle(author: Author, published: Boolean, title: String): Post?
}
