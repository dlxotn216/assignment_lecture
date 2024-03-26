package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.helper.AbstractDomainIntegrationTest
import io.taesu.lectureapi.helper.lectureTrainee
import io.taesu.lectureapi.helper.lectures
import io.taesu.lectureapi.lecture.domain.LectureRepository
import io.taesu.lectureapi.lecture.domain.LectureTraineeRepository
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeSearchCriteria
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class LectureTraineeSearchServiceTest: AbstractDomainIntegrationTest() {
    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var lectureTraineeRepository: LectureTraineeRepository

    @Autowired
    private lateinit var lectureTraineeSearchService: LectureTraineeSearchService

    @Test
    fun `강연 별 신청 목록을 조회할 수 있다`() {
        // given
        val (lectureKey1, lectureKey2, lectureKey3, lectureKey4) = saveLectures(lectureRepository)
        val traineeId = "00003"

        val lastKey = lectureTraineeRepository.saveAll(
            listOf(
                lectureTrainee(0L, lectureKey4, "00001"),   // (v)
                lectureTrainee(0L, lectureKey3, "00002"),   // (v)
                lectureTrainee(0L, lectureKey3, "00001"),   // (v)
                lectureTrainee(0L, lectureKey2, "00003"),   // lastKey
                lectureTrainee(0L, lectureKey2, "00004"),
                lectureTrainee(0L, lectureKey2, "00002"),
                lectureTrainee(0L, lectureKey2, "00001"),
                lectureTrainee(0L, lectureKey1, "00001"),
                lectureTrainee(0L, lectureKey1, "00002"),
                lectureTrainee(0L, lectureKey1, "00003"),
                lectureTrainee(0L, lectureKey1, "00004"),
                lectureTrainee(0L, lectureKey1, "00005"),
                lectureTrainee(0L, lectureKey1, "00006"),
            )
        ).find { it.matches(lectureKey2, traineeId) }!!.lectureTraineeKey

        // when
        val lectureTrainees = lectureTraineeSearchService.search(
            LectureTraineeSearchCriteria(
                size = 3,
                lastKey = lastKey
            )
        )

        // then
        lectureTrainees.run {
            assertThat(this.contents.size).isEqualTo(3)
            assertThat(this.contents[0].lectureKey).isEqualTo(lectureKey3)
            assertThat(this.contents[0].traineeId).isEqualTo("00001")

            assertThat(this.contents[1].lectureKey).isEqualTo(lectureKey3)
            assertThat(this.contents[1].traineeId).isEqualTo("00002")

            assertThat(this.contents[2].lectureKey).isEqualTo(lectureKey4)
            assertThat(this.contents[2].traineeId).isEqualTo("00001")
        }
    }

    internal data class LectureKeys4(
        val lectureKey1: Long,
        val lectureKey2: Long,
        val lectureKey3: Long,
        val lectureKey4: Long,
    )

    companion object {
        internal fun saveLectures(
            lectureRepository: LectureRepository,
        ): LectureKeys4 {
            val lectures = lectureRepository.saveAll(lectures(4))
            val lectureKey4 = lectures[3].lectureKey
            val lectureKey3 = lectures[2].lectureKey
            val lectureKey2 = lectures[1].lectureKey
            val lectureKey1 = lectures[0].lectureKey
            return LectureKeys4(lectureKey1, lectureKey2, lectureKey3, lectureKey4)
        }
    }

}
