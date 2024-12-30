package com.ll.backend.domain.youtube.query.playlistEntry.controller

import com.ll.backend.domain.youtube.query.playlistEntry.dto.YoutubePlaylistEntryDto
import com.ll.backend.domain.youtube.query.playlistEntry.service.YoutubeApiPlaylistEntryService
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.extensions.handleExceptions
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("apiV1FqYoutubePlaylistEntryController")
@RequestMapping("/api/v1/youtube/query/playlists/{playlistCode}/entries")
@Tag(name = "ApiV1FqYoutubePlaylistEntryController", description = "Query 전용 유튜브 재생목록엔트리 API 컨트롤러")
class ApiV1YoutubePlaylistEntryController(
    private val fqYoutubeApiPlaylistEntryService: YoutubeApiPlaylistEntryService
) {
    @GetMapping
    fun getItems(
        @PathVariable playlistCode: String
    ): List<YoutubePlaylistEntryDto> = handleExceptions {
        val playlistEntryDtos = fqYoutubeApiPlaylistEntryService
            .findByPlaylistCode(playlistCode)
            .getOrThrow()

        return playlistEntryDtos
    }

    @GetMapping("/clearCache")
    fun clearCache(
        @PathVariable playlistCode: String
    ): ResponseEntity<String> = handleExceptions {
        fqYoutubeApiPlaylistEntryService.clearCache(playlistCode)
        return ResponseEntity.ok("Cache cleared")
    }
}