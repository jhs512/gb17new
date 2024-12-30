package com.ll.backend.domain.youtube.command.playlist

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
class ApiV1YoutubePlaylistControllerTest(
    private val mockMvc: MockMvc
) {
    companion object {
        const val samplePlaylist1Code = "PLE0hRBClSk5I6oSjDBW3HJUAAT-4P6Qo8"
    }

    @Test
    @DisplayName("GET /api/v1/youtube/command/playlists/${samplePlaylist1Code}")
    fun t01() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/youtube/command/playlists/${samplePlaylist1Code}")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.channelId").value("UCiw6ewVgQkLMmgWjQp1-gKQ"))
            .andExpect(jsonPath("$.publishDate").value("2024-12-24T03:33:37.561282Z"))
            .andExpect(jsonPath("$.title").value("slog.gg/p/13916"))
            .andExpect(jsonPath("$.description").value(""))
    }
}