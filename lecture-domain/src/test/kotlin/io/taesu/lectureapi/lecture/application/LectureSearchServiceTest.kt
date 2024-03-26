package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.common.utils.localDateRange
import io.taesu.lectureapi.helper.AbstractDomainIntegrationTest
import io.taesu.lectureapi.helper.fixedReleasedAtLectures
import io.taesu.lectureapi.lecture.domain.LectureRepository
import io.taesu.lectureapi.lecture.infra.LectureSearchCriteria
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

/**
 * Created by itaesu on 2024/03/23.
 *
 * @author Lee Tae Su
 */
class LectureSearchServiceTest: AbstractDomainIntegrationTest() {
    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var lectureSearchService: LectureSearchService

    @Test
    fun `Lecture 검색 조건에 따른 검색 결과 테스트`() {
        // given
        lectureRepository.saveAll(fixedReleasedAtLectures())

        // when
        val nowDate = LocalDate.parse("2024-03-23")
        val localDateRange = nowDate localDateRange nowDate.plusDays(7L)
        val slicedResult =
            lectureSearchService.search(LectureSearchCriteria(releasedAt = localDateRange.toLocalDateTimeRange()))

        // then
        assertThat(slicedResult.contents.size).isEqualTo(2L)
        assertThat(slicedResult.contents[0].name).isEqualTo("lecture 4")
        assertThat(slicedResult.contents[1].name).isEqualTo("lecture 3")
    }

    @Test
    fun `Lecture id 목록 조건에 따른 검색 결과 테스트`() {
        // given
        val lectures = lectureRepository.saveAll(fixedReleasedAtLectures())

        // when
        val lectureVos =
            lectureSearchService.searchByLectureKeys(lectures.map { it.lectureKey })

        // then
        assertThat(lectureVos.size).isEqualTo(lectures.size)
    }

}
