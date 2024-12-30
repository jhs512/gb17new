package com.ll.backend.domain.youtube.query.playlist.service

import com.ll.backend.domain.youtube.query.playlist.dto.YoutubePlaylistDto
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service("fqYoutubeApiPlaylistService")
class YoutubeApiPlaylistService(
    private val youtubeApiPlaylistService: com.ll.backend.domain.youtube.command.playlist.service.YoutubeApiPlaylistService,
    private val cacheManager: CacheManager
) {
    @Cacheable("fqYoutubeApiPlaylistService__youtubePlaylist", key = "#code")
    fun findByCode(code: String): YoutubePlaylistDto? {
        return youtubeApiPlaylistService.findByCode(code)?.let {
            YoutubePlaylistDto(
                code = it.code,
                channelCode = it.channelCode,
                publishDate = it.publishDate,
                title = it.title,
                description = it.description
            )
        }
    }

    fun clearCache(code: String) {
        cacheManager.getCache("fqYoutubeApiPlaylistService__youtubePlaylist")?.evict(code)
    }
}