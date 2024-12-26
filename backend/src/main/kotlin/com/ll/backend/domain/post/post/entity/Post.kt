package com.ll.backend.domain.post.post.entity

import com.ll.backend.domain.post.author.entity.Author
import com.ll.backend.global.jpa.entity.BaseTime
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
class Post(
    @ManyToOne(fetch = LAZY)
    var author: Author,

    @Column(length = 100)
    var title: String,

    var published: Boolean = false
) : BaseTime() {
    fun modify(title: String, body: String, published: Boolean) {
        this.title = title
        this.published = published
        if (this.content != body) {
            setModified()
            this.content = body
        }
    }

    @OneToOne(fetch = LAZY, mappedBy = "post", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    @PrimaryKeyJoinColumn
    lateinit var body: PostBody

    var content: String
        get() = body.content
        set(value) {
            body.content = value
        }
}
