package com.ll.backend.domain.youtube.command.playlistEntry.controller

import com.ll.backend.domain.youtube.command.playlistEntry.dto.YoutubePlaylistEntryDto
import com.ll.backend.domain.youtube.command.playlistEntry.service.YoutubeApiPlaylistEntryService
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.extensions.handleExceptions
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/youtube/command/playlists/{playlistCode}/entries")
@Tag(name = "ApiV1YoutubePlaylistEntryController", description = "Command용 유튜브 재생목록엔트리 API 컨트롤러")
class ApiV1YoutubePlaylistEntryController(
    private val youtubeApiPlaylistEntryService: YoutubeApiPlaylistEntryService
) {
    @GetMapping
    fun getItems(
        @PathVariable playlistCode: String
    ): List<YoutubePlaylistEntryDto> = handleExceptions {
        val playlistEntryDtos = youtubeApiPlaylistEntryService
            .findByPlaylistCode(playlistCode)
            .getOrThrow()

        return playlistEntryDtos
    }
}