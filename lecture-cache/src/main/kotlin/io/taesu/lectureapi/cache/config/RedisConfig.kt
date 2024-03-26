package io.taesu.lectureapi.cache.config

import io.taesu.lectureapi.cache.config.properties.RedisProperties
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer


/**
 * Created by itaesu on 2024/02/09.
 *
 * @author Lee Tae Su
 * @version redis-spring
 * @since redis-spring
 */
@Configuration
class RedisConfig {
    @Bean
    fun redissonClient(redisInfo: RedisProperties): RedissonClient {
        val config = Config()
        config.useSingleServer().setAddress("redis://${redisInfo.host}:${redisInfo.port}")
            .setPassword(redisInfo.password)
            .setClientName(redisInfo.clientName)
        return Redisson.create(config)
    }

    @Bean
    fun redisConnectionFactory(redisInfo: RedisProperties): RedisConnectionFactory {
        return RedissonConnectionFactory(redissonClient(redisInfo))
    }

    @Bean
    fun redisTemplate(redisInfo: RedisProperties): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            this.keySerializer = RedisSerializer.string()
            this.valueSerializer = RedisSerializer.string()

            this.hashKeySerializer = RedisSerializer.string()
            this.hashValueSerializer = RedisSerializer.string()

            this.setDefaultSerializer(RedisSerializer.string())
            this.connectionFactory = redisConnectionFactory(redisInfo)
        }
    }

    @Bean
    fun redisStringTemplate(redisInfo: RedisProperties): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory(redisInfo))
    }
}

