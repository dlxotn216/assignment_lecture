package io.taesu.lectureapi.application

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.taesu.lectureapi.application.PopularLectureService.Companion.RECENTLY_DAYS
import io.taesu.lectureapi.cache.infra.StringRedisRepository
import io.taesu.lectureapi.cache.infra.dtos.SortedSetIncrementOperation
import io.taesu.lectureapi.common.utils.localDateRange
import io.taesu.lectureapi.helper.LECTURE_KEY
import io.taesu.lectureapi.helper.requestContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
@ExtendWith(MockKExtension::class)
class PopularLectureServiceTest {
    @MockK
    private lateinit var stringRedisRepository: StringRedisRepository

    @SpyK
    @InjectMockKs
    private lateinit var service: PopularLectureService

    @Test
    fun `인기 강연 조회는 강연의 키와 스코어를 반환한다`() {
        // given
        val nowDate = requestContext().nowDate
        val expected = listOf("1" to 1.0, "2" to 2.0, "3" to 3.0)
        every {
            stringRedisRepository.zRange(
                PopularLectureService.resolvePopularLectureKey(nowDate),
                0L,
                -1L
            )
        } returns expected

        // when
        val popularLectureKeyScores = service.retrieveTodayPopularLectureKeys(nowDate)

        // then
        assertThat(popularLectureKeyScores).isEqualTo(
            listOf(1L to 1.0, 2L to 2.0, 3L to 3.0)
        )
    }


    @Test
    fun `강연 신청에 따른 인기 강연 집계는 오늘로부터 RECENTLY_DAYS 이후까지 사전 집계한다`() {
        // given
        val lectureKey = LECTURE_KEY
        val requestContext = requestContext()

        val slot = slot<SortedSetIncrementOperation>()
        every { stringRedisRepository.zIncrAll(capture(slot)) } answers { listOf() }

        // when
        service.increase(lectureKey, requestContext)

        // then
        slot.captured.run {
            val nowDate = requestContext.nowDate
            val localDateRange = nowDate localDateRange nowDate.plusDays(RECENTLY_DAYS)
            val expected = SortedSetIncrementOperation(
                localDateRange.getDates().map(PopularLectureService.Companion::resolvePopularLectureKey),
                1L,
                lectureKey.toString()
            )
            assertThat(this).isEqualTo(expected)
        }
    }

    @Test
    fun `강연 신청 취소에 따른 인기 강연 집계는 오늘로부터 RECENTLY_DAYS 이후까지 사전 집계한다`() {
        // given
        val lectureKey = LECTURE_KEY
        val requestContext = requestContext()

        val slot = slot<SortedSetIncrementOperation>()
        every { stringRedisRepository.zIncrAll(capture(slot)) } answers { listOf() }

        // when
        service.decrease(lectureKey, requestContext)

        // then
        slot.captured.run {
            val nowDate = requestContext.nowDate
            val localDateRange = nowDate localDateRange nowDate.plusDays(RECENTLY_DAYS)
            val expected = SortedSetIncrementOperation(
                localDateRange.getDates().map(PopularLectureService.Companion::resolvePopularLectureKey),
                -1L,
                lectureKey.toString()
            )
            assertThat(this).isEqualTo(expected)
        }
    }
}
