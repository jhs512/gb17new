package com.ll.backend.domain.member.member.dto

import com.ll.backend.domain.member.member.entity.Member
import java.time.LocalDateTime

data class MemberDto(
    val id: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val nickname: String,
    val name: String,

    ) {
    constructor(member: Member) : this(
        id = member.id,
        nickname = member.nickname,
        name = member.name,
        createDate = member.createDate,
        modifyDate = member.modifyDate
    )
}
