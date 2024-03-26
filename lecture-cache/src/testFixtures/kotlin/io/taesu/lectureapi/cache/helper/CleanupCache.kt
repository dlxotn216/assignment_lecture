package io.taesu.lectureapi.cache.helper

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
@Component
class CleanupCache(
    private val redisStringTemplate: StringRedisTemplate,
) {
    fun all() {
        redisStringTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }
}
