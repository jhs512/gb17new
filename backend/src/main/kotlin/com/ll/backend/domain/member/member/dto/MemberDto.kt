package com.ll.backend.domain.member.member.dto

import com.ll.backend.domain.member.member.entity.Member
import java.time.LocalDateTime

data class MemberDto(
    val id: Long,
    val createDate: LocalDateTime,
    val modifyDate: LocalDateTime,
    val nickname: String,
    val name: String,
    val profileImgUrl: String,
    val authorities: List<String>,
    val social: Boolean
) {
    constructor(member: Member) : this(
        id = member.id,
        nickname = member.nickname,
        name = member.name,
        createDate = member.createDate,
        modifyDate = member.modifyDate,
        profileImgUrl = member.profileImgUrlOrDefault,
        authorities = member.authorities.map { it.authority },
        social = member.username.startsWith("KAKAO__")
    )
}
