package com.ll.backend.global.cache

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@Profile("prod")
@EnableCaching
@EnableConfigurationProperties(CacheConfigPropertiesList::class)
class ProdCacheConfig(
    private val cacheConfigPropertiesList: CacheConfigPropertiesList
) {
    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val objectMapper = ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .activateDefaultTyping(
                LaissezFaireSubTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
            )

        val defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                SerializationPair.fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer(objectMapper))
            )
            .prefixCacheNameWith("app::")

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultCacheConfig)
            .withInitialCacheConfigurations(cacheConfigPropertiesList.getCacheConfigurations(defaultCacheConfig))
            .build()
    }
} 