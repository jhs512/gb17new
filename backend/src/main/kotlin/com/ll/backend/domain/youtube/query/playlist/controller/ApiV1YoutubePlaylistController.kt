package com.ll.backend.domain.youtube.query.playlist.controller

import com.ll.backend.domain.youtube.query.playlist.dto.YoutubePlaylistDto
import com.ll.backend.domain.youtube.query.playlist.service.YoutubeApiPlaylistService
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.extensions.handleExceptions
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("apiV1FqYoutubePlaylistController")
@RequestMapping("/api/v1/youtube/query/playlists")
@Tag(name = "ApiV1FqYoutubePlaylistController", description = "Query 유튜브 재생목록 API 컨트롤러")
class ApiV1YoutubePlaylistController(
    private val fqYoutubeApiPlaylistService: YoutubeApiPlaylistService
) {
    @GetMapping("/{code}")
    fun getItem(
        @PathVariable code: String
    ): YoutubePlaylistDto = handleExceptions {
        val playlistDto = fqYoutubeApiPlaylistService.findByCode(code).getOrThrow()

        return playlistDto
    }
}