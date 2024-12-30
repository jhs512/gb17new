package com.ll.backend.global.cache

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!prod")
@EnableCaching
@EnableConfigurationProperties(CacheConfigPropertiesList::class)
class NotProdCacheConfig(
    private val cacheConfigPropertiesList: CacheConfigPropertiesList
) {
    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager(*cacheConfigPropertiesList.getCacheNames())
    }
}