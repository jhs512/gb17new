package com.ll.backend.global.cache

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.data.redis.cache.RedisCacheConfiguration
import java.time.Duration

@ConfigurationProperties(prefix = "custom.cache")
data class CacheConfigPropertiesList(
    val shortLived: CacheConfigProperties,
    val longLived: CacheConfigProperties
) {
    private val cacheInfos = mapOf(
        "fqYoutubeApiPlaylistEntryService__youtubePlaylistEntries" to longLived,
        "fqYoutubeApiPlaylistService__youtubePlaylist" to longLived
    )

    fun getCacheNames(): Array<String> = cacheInfos.keys.toTypedArray()

    fun getCacheConfigurations(defaultConfig: RedisCacheConfiguration): Map<String, RedisCacheConfiguration> {
        return cacheInfos.mapValues { (_, config) ->
            defaultConfig.entryTtl(Duration.ofSeconds(config.ttl.toLong()))
        }
    }
}