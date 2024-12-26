package com.ll.backend.domain.post.author.entity

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.global.jpa.entity.BaseTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "member")
class Author(
    @Column(name = "nickname")
    var name: String
) : BaseTime() {
    constructor(member: Member) : this(member.nickname) {
        this.id = member.id
        this.createDate = member.createDate
        this.modifyDate = member.modifyDate
    }
}