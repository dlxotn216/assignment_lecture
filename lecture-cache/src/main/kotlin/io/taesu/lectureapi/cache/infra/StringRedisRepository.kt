package io.taesu.lectureapi.cache.infra

import io.taesu.lectureapi.cache.infra.dtos.SortedSetIncrementOperation

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
interface StringRedisRepository {
    fun incr(key: String): Long

    fun zRange(key: String, start: Long, size: Long): List<Pair<String, Double>>

    fun zIncrAll(operation: SortedSetIncrementOperation): List<Long>

    fun decr(key: String): Long
}
