package com.ll.backend.domain.youtube.command.playlistEntry.service

import com.github.salomonbrys.kotson.get
import com.google.gson.JsonParser
import com.ll.backend.domain.youtube.command.playlistEntry.dto.YoutubePlaylistEntryDto
import com.ll.backend.global.youtube.YoutubeConfig
import com.ll.backend.standard.extensions.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class YoutubeApiPlaylistEntryService(
    val restClient: RestClient
) {
    fun findByPlaylistCode(playlistCode: String): List<YoutubePlaylistEntryDto> {
        val dtos = mutableListOf<YoutubePlaylistEntryDto>()
        var currentPageToken: String? = null

        do {
            val uriBuilder = StringBuilder()
                .append("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet")
                .append("&playlistId=${playlistCode}")
                .append("&key=${YoutubeConfig.apiKey}")
                .append("&maxResults=50")

            currentPageToken?.let {
                uriBuilder.append("&pageToken=$it")
            }

            val pageResponseBody = restClient.get()
                .uri(uriBuilder.toString())
                .retrieve()
                .body<String>()

            val pageBodyJson = JsonParser.parseString(pageResponseBody.getOrThrow())
                .asJsonObject

            val items = pageBodyJson["items"].asJsonArray

            items.forEach { item ->
                val title = item["snippet"]["title"].asString
                val code = item["snippet"]["resourceId"]["videoId"].asString
                val position = item["snippet"]["position"].asInt

                dtos.add(
                    YoutubePlaylistEntryDto(
                        code = code,
                        title = title,
                        position = position
                    )
                )
            }

            currentPageToken = if (pageBodyJson.has("nextPageToken")) {
                pageBodyJson["nextPageToken"].asString
            } else {
                null
            }

        } while (currentPageToken != null)

        return dtos
    }
}

