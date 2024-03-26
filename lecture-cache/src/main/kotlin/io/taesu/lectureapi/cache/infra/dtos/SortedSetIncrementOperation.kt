package io.taesu.lectureapi.cache.infra.dtos

/**
 * Created by itaesu on 2024/03/23.
 *
 * @author Lee Tae Su
 */
data class SortedSetIncrementOperation(
    val keys: List<String>,
    val increment: Long,
    val member: String,
)
