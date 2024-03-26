package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.common.exception.ErrorCode
import io.taesu.lectureapi.helper.*
import io.taesu.lectureapi.lecture.domain.LectureRepository
import io.taesu.lectureapi.lecture.domain.LectureTraineeRepository
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class LectureRetrieveServiceTest: AbstractDomainIntegrationTest() {
    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var lectureTraineeRepository: LectureTraineeRepository

    @Autowired
    private lateinit var lectureRetrieveService: LectureRetrieveService

    @Test
    fun `lectureKey로 Lecture를 조회할 수 있다`() {
        // given
        val lecture = lecture(0L)
        val lectureKey = lectureRepository.save(lecture).lectureKey

        // when
        val retrieved = lectureRetrieveService.retrieve(lectureKey)

        // then
        assertThat(retrieved.name).isEqualTo(lecture.name)
        assertThat(retrieved.location).isEqualTo(lecture.location)
        assertThat(retrieved.releasedAt).isEqualTo(lecture.releasedAt)
        assertThat(retrieved.trainer).isEqualTo(lecture.trainer)
        assertThat(retrieved.maxTraineeCount).isEqualTo(lecture.maxTraineeCount)
    }

    @Test
    fun `존재하지 않는 lectureKey로 조회 시 예외를 던진다`() {
        // given
        // when
        val exception = assertThrows<BusinessException> {
            lectureRetrieveService.retrieve(123123L)
        }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND)
    }

    @Test
    fun `lectureKey 목록으로 LectureVo 목록을 조회할 수 있다`() {
        // given
        val lectures = lectureRepository.saveAll(lectures(5))

        // when
        val lectureVos = lectureRetrieveService.retrieveAllVo(lectures.map { it.lectureKey })

        // then
        assertThat(lectureVos.size).isEqualTo(lectures.size)
    }

    @Test
    fun `lectureKey, traineeId로 LectureTrainee 존재 여부를 조회할 수 있다`() {
        // given
        lectureTraineeRepository.save(lectureTrainee(0L))

        // when
        val exists = lectureRetrieveService.existsLectureTrainee(LECTURE_KEY, TRAINEE_ID)

        // then
        assertThat(exists).isTrue()
    }

    @Test
    fun `존재하지 않는 lectureKey, traineeId는 false를 반환한다`() {
        // given
        // when
        val exists = lectureRetrieveService.existsLectureTrainee(123132L, "00000")

        // then
        assertThat(exists).isFalse()
    }

    @Test
    fun `lectureKey, traineeId로 LectureTrainee를 조회할 수 있다`() {
        // given
        lectureTraineeRepository.save(lectureTrainee(0L))

        // when
        val lectureTrainee = lectureRetrieveService.retrieveLectureTrainee(LECTURE_KEY, TRAINEE_ID)

        // then
        assertThat(lectureTrainee).isNotNull
    }

    @Test
    fun `존재하지 않는 lectureKey, traineeId를 조회하면 예외를 던진다`() {
        // given
        // when
        val exception = assertThrows<BusinessException> {
            lectureRetrieveService.retrieveLectureTrainee(LECTURE_KEY, TRAINEE_ID)
        }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.RESOURCE_NOT_FOUND)
    }
}
