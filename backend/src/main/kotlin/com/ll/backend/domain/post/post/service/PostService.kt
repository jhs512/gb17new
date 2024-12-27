package com.ll.backend.domain.post.post.service

import com.ll.backend.domain.post.author.entity.Author
import com.ll.backend.domain.post.post.entity.Post
import com.ll.backend.domain.post.post.entity.PostBody
import com.ll.backend.domain.post.post.repository.PostRepository
import com.ll.backend.global.exceptions.ServiceException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun findById(id: Long): Post? {
        return postRepository.findById(id).orElse(null)
    }

    fun count(): Long {
        return postRepository.count()
    }

    fun write(author: Author, title: String, body: String, published: Boolean = false): Post {
        val post = Post(
            author = author,
            title = title,
            published = published
        )

        post.body = PostBody(
            post = post,
            content = body
        )

        return postRepository.save(post)
    }

    fun findByAuthorAndSearchKeywordPaged(
        author: Author,
        searchKeyword: String,
        page: Int,
        pageSize: Int
    ): Page<Post> {
        val pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")))

        if (searchKeyword.isBlank()) {
            return postRepository.findByAuthor(
                author,
                pageable
            )
        }

        return postRepository.findByAuthorAndBody_ContentLike(
            author,
            "%$searchKeyword%",
            pageable
        )
    }

    fun findByPublishedPaged(published: Boolean, page: Int, pageSize: Int): Page<Post> {
        return findByPublishedAndSearchKeywordPaged(
            published,
            "",
            page,
            pageSize
        )
    }

    fun findByPublishedAndSearchKeywordPaged(
        published: Boolean,
        searchKeyword: String,
        page: Int,
        pageSize: Int
    ): Page<Post> {
        val pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")))

        if (searchKeyword.isBlank()) {
            return postRepository.findByPublished(
                published,
                pageable
            )
        }

        return postRepository.findByPublishedAndBody_ContentLike(
            published,
            "%$searchKeyword%",
            pageable
        )
    }

    fun delete(post: Post) {
        postRepository.delete(post)
    }

    fun modify(post: Post, title: String, content: String, published: Boolean): Post {
        post.modify(title, content, published)

        return post
    }

    fun checkPermissionToDelete(actor: Author, post: Post) {
        if (actor != post.author) throw ServiceException("403-1", "글의 작성자만 삭제할 수 있습니다.")
    }

    fun checkPermissionToModify(actor: Author, post: Post) {
        if (actor != post.author) throw ServiceException("403-1", "글의 작성자만 수정할 수 있습니다.")
    }

    fun checkPermissionToWrite(actor: Author) {
        // 로그인이 되었다면 누구나 가능
    }

    fun checkPermissionToRead(actor: Author, post: Post) {
        if (post.published) return

        if (actor != post.author) throw ServiceException("403-1", "비공개글은 작성자만 조회할 수 있습니다.")
    }
}
