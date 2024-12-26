package com.ll.backend.domain.post.post.entity

import com.ll.backend.global.jpa.entity.BaseEntity
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import org.hibernate.Hibernate
import org.springframework.data.domain.Persistable

@Entity
class PostBody(
    @Id
    var id: Long = 0,

    @OneToOne(fetch = LAZY)
    @MapsId
    @JoinColumn(name = "id")
    var post: Post,

    @Column(columnDefinition = "TEXT")
    var content: String
) : Persistable<Long> {
    override fun getId(): Long = id

    override fun isNew(): Boolean = id == 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as BaseEntity

        return id == other.getId()
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}