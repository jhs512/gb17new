package com.ll.backend.domain.youtube.command.playlist.controller

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiV1YoutubePlaylistControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    companion object {
        const val samplePlaylist1Code = "PLE0hRBClSk5I6oSjDBW3HJUAAT-4P6Qo8"
        const val samplePlaylist1ChannelCode = "UCiw6ewVgQkLMmgWjQp1-gKQ"
        const val samplePlaylist1PublishDate = "2024-12-24T01:56:37.721868"
        const val samplePlaylist1Title = "slog.gg/p/13915"
        const val samplePlaylist1Description = ""
    }

    @Test
    @DisplayName("GET /api/v1/youtube/command/playlists/$samplePlaylist1Code")
    fun t01() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/youtube/command/playlists/$samplePlaylist1Code")
            )
            .andDo(MockMvcResultHandlers.print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value(samplePlaylist1Code))
            .andExpect(jsonPath("$.channelCode").value(samplePlaylist1ChannelCode))
            .andExpect(jsonPath("$.publishDate").value(samplePlaylist1PublishDate))
            .andExpect(jsonPath("$.title").value(samplePlaylist1Title))
            .andExpect(jsonPath("$.description").value(samplePlaylist1Description))
    }

    @Test
    @DisplayName("GET /api/v1/youtube/command/playlists/NOT-EXISTS")
    fun t02() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/youtube/command/playlists/NOT-EXISTS")
            )
            .andDo(MockMvcResultHandlers.print())

        // THEN
        resultActions
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.resultCode").value("404-1"))
    }
}