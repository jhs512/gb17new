package com.ll.backend.domain.youtube.command.playlist.service

import com.github.salomonbrys.kotson.get
import com.google.gson.JsonParser
import com.ll.backend.domain.youtube.command.playlist.dto.YoutubePlaylistDto
import com.ll.backend.global.youtube.YoutubeConfig
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.extensions.toLocalDateTime
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class YoutubeApiPlaylistService(
    val restClient: RestClient
) {
    fun findByCode(code: String): YoutubePlaylistDto? {
        val responseBody = restClient.get()
            .uri("https://www.googleapis.com/youtube/v3/playlists?part=snippet&id=${code}&key=${YoutubeConfig.apiKey}")
            .retrieve()
            .body<String>()

        val bodyJson = JsonParser.parseString(responseBody.getOrThrow())
            .asJsonObject

        if (bodyJson["items"].asJsonArray.isEmpty)
            return null

        val title = bodyJson["items"][0]["snippet"]["title"].asString
        val channelCode = bodyJson["items"][0]["snippet"]["channelId"].asString
        val description = bodyJson["items"][0]["snippet"]["description"].asString
        val publishDate = bodyJson["items"][0]["snippet"]["publishedAt"].asString.toLocalDateTime()

        return YoutubePlaylistDto(
            code = code,
            channelCode = channelCode,
            title = title,
            description = description,
            publishDate = publishDate
        )
    }
}