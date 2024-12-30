package com.ll.backend.domain.youtube.command.playlist.controller

import com.ll.backend.domain.youtube.command.playlist.dto.YoutubePlaylistDto
import com.ll.backend.domain.youtube.command.playlist.service.YoutubeApiPlaylistService
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.extensions.handleExceptions
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/youtube/command/playlists")
@Tag(name = "ApiV1YoutubePlaylistController", description = "Command용 유튜브 재생목록 API 컨트롤러")
class ApiV1YoutubePlaylistController(
    private val youtubeApiPlaylistService: YoutubeApiPlaylistService
) {
    @GetMapping("/{code}")
    fun getItem(
        @PathVariable code: String
    ): YoutubePlaylistDto = handleExceptions {
        val playlistDto = youtubeApiPlaylistService.findByCode(code).getOrThrow()

        return playlistDto
    }
}