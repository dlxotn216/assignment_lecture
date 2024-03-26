package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.helper.AbstractDomainIntegrationTest
import io.taesu.lectureapi.helper.lectureTrainee
import io.taesu.lectureapi.lecture.application.LectureTraineeSearchServiceTest.Companion.saveLectures
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
class MyLectureSearchServiceTest: AbstractDomainIntegrationTest() {
    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var lectureTraineeRepository: LectureTraineeRepository

    @Autowired
    private lateinit var myLectureSearchService: MyLectureSearchService

    @Test
    fun `나의 강연 목록을 조회할 수 있다`() {
        // given
        val (lectureKey1, lectureKey2, lectureKey3, lectureKey4) = saveLectures(lectureRepository)
        val traineeId = "00001"

        val lectureTrainees = lectureTraineeRepository.saveAll(
            listOf(
                lectureTrainee(0L, lectureKey4, "00001"),  // (v)
                lectureTrainee(0L, lectureKey3, "00002"),
                lectureTrainee(0L, lectureKey3, "00001"),  // (v)
                lectureTrainee(0L, lectureKey2, "00003"),
                lectureTrainee(0L, lectureKey2, "00004"),
                lectureTrainee(0L, lectureKey2, "00002"),
                lectureTrainee(0L, lectureKey2, "00001"),  // lastKey
                lectureTrainee(0L, lectureKey1, "00001"),
                lectureTrainee(0L, lectureKey1, "00002"),
                lectureTrainee(0L, lectureKey1, "00003"),
                lectureTrainee(0L, lectureKey1, "00004"),
                lectureTrainee(0L, lectureKey1, "00005"),
                lectureTrainee(0L, lectureKey1, "00006"),
            )
        )
        val lastKey = lectureTrainees.find { it.matches(lectureKey2, traineeId) }!!.lectureTraineeKey

        // when
        val myLectures = myLectureSearchService.search(
            LectureTraineeSearchCriteria(
                traineeId = traineeId,
                size = 3,
                lastKey = lastKey
            )
        )

        // then
        myLectures.run {
            assertThat(this.contents.size).isEqualTo(2)
            assertThat(this.contents[0].lectureKey).isEqualTo(lectureKey3)
            assertThat(this.contents[1].lectureKey).isEqualTo(lectureKey4)
        }
    }

}
