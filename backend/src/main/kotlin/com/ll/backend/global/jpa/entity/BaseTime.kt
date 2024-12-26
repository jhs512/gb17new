package com.ll.backend.global.jpa.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTime : BaseEntity() {
    @CreatedDate
    lateinit var createDate: LocalDateTime

    @LastModifiedDate
    lateinit var modifyDate: LocalDateTime

    fun setModified() {
        modifyDate = LocalDateTime.now()
    }
}
