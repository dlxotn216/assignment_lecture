package io.taesu.lectureapi.application

import io.taesu.lectureapi.cache.infra.StringRedisRepository
import io.taesu.lectureapi.cache.infra.dtos.SortedSetIncrementOperation
import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.common.utils.localDateRange
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * Created by itaesu on 2024/03/23.
 *
 * 인기 강연 관리 서비스
 *
 * @author Lee Tae Su
 */
@Service
class PopularLectureService(private val stringRedisRepository: StringRedisRepository) {
    fun retrieveTodayPopularLectureKeys(nowDate: LocalDate): List<Pair<Long, Double>> {
        // zrange popular-lecture:20240323 0 -1 rev withscores (전체 조회)
        return stringRedisRepository.zRange(resolvePopularLectureKey(nowDate), 0L, -1L)
            .map { it.first.toLong() to it.second }
    }

    fun increase(lectureKey: Long, requestContext: RequestContext) {
        stringRedisRepository.zIncrAll(resolveOperation(lectureKey, 1L, requestContext.nowDate))
    }

    fun decrease(lectureKey: Long, requestContext: RequestContext) {
        stringRedisRepository.zIncrAll(resolveOperation(lectureKey, -1L, requestContext.nowDate))
    }

    private fun resolveOperation(
        lectureKey: Long,
        increment: Long,
        nowDate: LocalDate,
    ): SortedSetIncrementOperation {
        val localDateRange = nowDate localDateRange nowDate.plusDays(RECENTLY_DAYS)
        return SortedSetIncrementOperation(
            localDateRange.getDates().map(::resolvePopularLectureKey),
            increment,
            lectureKey.toString()
        )
    }

    companion object {
        const val RECENTLY_DAYS = 2L
        private val KEY_DATE_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        fun resolvePopularLectureKey(it: LocalDate): String {
            return "popular-lecture:${it.format(PopularLectureService.KEY_DATE_PATTERN)}"
        }
    }
}
