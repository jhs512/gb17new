package com.ll.backend.global.youtube

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class YoutubeConfig {
    companion object {
        lateinit var apiKey: String
    }

    @Value("\${custom.youtube.apiKey}")
    fun setApiKey(apiKey: String) {
        Companion.apiKey = apiKey
    }
} 