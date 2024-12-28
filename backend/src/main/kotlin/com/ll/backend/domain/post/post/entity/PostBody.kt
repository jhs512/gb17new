package com.ll.backend.domain.post.post.entity

import com.ll.backend.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne

@Entity
class PostBody(

    @ManyToOne(fetch = LAZY)
    var post: Post,

    @Column(columnDefinition = "TEXT")
    var content: String
) : BaseEntity()