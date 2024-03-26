package io.taesu.lectureapi.cache.infra.impl

import io.taesu.lectureapi.cache.infra.StringRedisRepository
import io.taesu.lectureapi.cache.infra.dtos.SortedSetIncrementOperation
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Repository

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@Repository
class RedisTemplateStringRepository(private val redisStringTemplate: StringRedisTemplate): StringRedisRepository {
    override fun incr(key: String): Long {
        return redisStringTemplate.opsForValue().increment(key) ?: 0L
    }

    override fun zRange(key: String, start: Long, size: Long): List<Pair<String, Double>> {
        val reverseRangeWithScores = redisStringTemplate.opsForZSet().reverseRangeWithScores(key, start, size)
            ?.toList()
            ?: emptyList()
        return reverseRangeWithScores.mapNotNull {
            val value = it.value ?: return@mapNotNull null
            val score = it.score ?: return@mapNotNull null
            value to score
        }
    }

    /**
     * ZINCRBY key increment member 일괄 실행을 위한 lua script 구현체
     */
    override fun zIncrAll(operation: SortedSetIncrementOperation): List<Long> {
        val operations =
            List(operation.keys.size) { index ->
                "redis.call('ZINCRBY', KEYS[${index + 1}], KEYS[${operation.keys.size + 1}], KEYS[${operation.keys.size + 2}])"
            }.joinToString(",")
        /*
        redis.call('ZINCRBY', KEYS[1], KEYS[4], KEYS[5]),
        redis.call('ZINCRBY', KEYS[2], KEYS[4], KEYS[5]),
        redis.call('ZINCRBY', KEYS[3], KEYS[4], KEYS[5]),
         */
        val script = RedisScript<List<String?>>(
            """
                return 
                {
                    $operations
                }
            """.trimIndent()
        )
        val args = operation.keys + operation.increment.toString() + operation.member
        val execute = redisStringTemplate.execute(script, args)
        return execute.mapNotNull { it?.toLongOrNull() }
    }


    override fun decr(key: String): Long {
        return redisStringTemplate.opsForValue().decrement(key) ?: 0L
    }
}
