package com.ll.backend.global.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.Hibernate

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    var _id: Long = 0

    val id: Long
        get() = _id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as BaseEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}