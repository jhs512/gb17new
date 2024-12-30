package com.ll.backend.domain.youtube.query.playlistEntry.service

import com.ll.backend.domain.youtube.query.playlistEntry.dto.YoutubePlaylistEntryDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service("fqYoutubeApiPlaylistEntryService")
class YoutubeApiPlaylistEntryService(
    val youtubeApiPlaylistEntryService: com.ll.backend.domain.youtube.command.playlistEntry.service.YoutubeApiPlaylistEntryService
) {
    @Cacheable("fqYoutubeApiPlaylistEntryService__youtubePlaylistEntries", key = "#playlistCode")
    fun findByPlaylistCode(playlistCode: String): List<YoutubePlaylistEntryDto> {
        return youtubeApiPlaylistEntryService.findByPlaylistCode(playlistCode).let {
            return it.map {
                YoutubePlaylistEntryDto(
                    code = it.code,
                    title = it.title,
                    position = it.position
                )
            }
        }
    }
}

