package com.ll.backend.domain.youtube.command.playlist.dto

import java.time.LocalDateTime

data class YoutubePlaylistDto(
    val code: String,
    val channelCode: String,
    val publishDate: LocalDateTime,
    val title: String,
    val description: String,
) {
}