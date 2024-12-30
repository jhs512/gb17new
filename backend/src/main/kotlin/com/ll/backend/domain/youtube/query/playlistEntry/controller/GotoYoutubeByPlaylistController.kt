package com.ll.backend.domain.youtube.query.playlistEntry.controller

import com.ll.backend.domain.youtube.query.playlistEntry.service.YoutubeApiPlaylistEntryService
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.extensions.handleExceptions
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/goto/youtube/{playlistCode}")
class GotoYoutubeByPlaylistController(
    private val fqYoutubeApiPlaylistEntryService: YoutubeApiPlaylistEntryService
) {
    @GetMapping("/{position}")
    fun gotoYoutubeByPosition(
        @PathVariable playlistCode: String,
        @PathVariable position: Int
    ): ResponseEntity<String> = handleExceptions {
        val playlistEntryDtos = fqYoutubeApiPlaylistEntryService
            .findByPlaylistCode(playlistCode)
            .getOrThrow()

        val playlistEntryDto = playlistEntryDtos[position]
        val url = "https://youtu.be/${playlistEntryDto.code}"

        // ResponseEntity 생성
        return ResponseEntity.status(HttpStatus.FOUND) // 302 상태 코드
            .header(HttpHeaders.LOCATION, url) // 리다이렉션 URL 설정
            .header(HttpHeaders.CACHE_CONTROL, "max-age=300") // 5분 동안 캐시
            .build()
    }
}