package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.common.exception.ErrorCode
import io.taesu.lectureapi.helper.TRAINEE_ID
import io.taesu.lectureapi.helper.lecture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class LectureTest {
    @Test
    fun `신청이 완료되면 잔여 좌석이 줄어든다`() {
        // given
        val nowDateTime = LocalDateTime.parse("2024-03-22T00:00:00")
        val lecture = lecture(
            maxTraineeCount = 100,
            releasedAt = nowDateTime.plusDays(1)
        )

        // when
        val lectureTrainee = lecture.apply(TRAINEE_ID, nowDateTime)

        // then
        assertThat(lectureTrainee.lectureKey).isEqualTo(lecture.lectureKey)
        assertThat(lectureTrainee.traineeId).isEqualTo(TRAINEE_ID)
        assertThat(lecture.remainTraineeCount).isEqualTo(99)
    }

    @Test
    fun `신청이 마감된 강연은 신청할 수 없다`() {
        // given
        val nowDateTime = LocalDateTime.parse("2024-03-22T00:00:00")
        val lecture = lecture(
            releasedAt = nowDateTime.minusDays(1)
        )

        // when
        val exception = assertThrows<BusinessException> {
            lecture.apply(TRAINEE_ID, nowDateTime)
        }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.LECTURE_CLOSED)
    }

    @Test
    fun `신청인원이 가득 찬 강연은 신청할 수 없다`() {
        // given
        val nowDateTime = LocalDateTime.parse("2024-03-22T00:00:00")
        val lecture = lecture(
            maxTraineeCount = 1,
            releasedAt = nowDateTime.plusDays(1)
        )
        lecture.apply(TRAINEE_ID, nowDateTime)

        // when
        val exception = assertThrows<BusinessException> {
            lecture.apply("traine2", nowDateTime)
        }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.STATE_IS_FULL)
    }

    @Test
    fun `진행 완료된 강연은 신청 취소 할 수 없다`() {
        // given
        val nowDateTime = LocalDateTime.parse("2024-03-22T00:00:00")
        val lecture = lecture(
            releasedAt = nowDateTime.minusDays(1),
            maxTraineeCount = 20,
            remainTraineeCount = 19
        )

        // when
        val exception = assertThrows<BusinessException> {
            lecture.cancel(TRAINEE_ID, nowDateTime)
        }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.LECTURE_CLOSED)
    }

    @Test
    fun `강연은 초과 신청 취소 할 수 없다`() {
        // given
        val nowDateTime = LocalDateTime.parse("2024-03-22T00:00:00")
        val lecture = lecture(
            releasedAt = nowDateTime.plusDays(1),
            maxTraineeCount = 20,
            remainTraineeCount = 20
        )

        // when
        val exception = assertThrows<BusinessException> {
            lecture.cancel(TRAINEE_ID, nowDateTime)
        }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.UNEXPECTED)
    }

    @Test
    fun `LectureContent 정보가 없다면 새로 생성한다`() {
        // given
        val lecture = lecture()

        // when
        lecture.setContentValue("123123")

        // then
        assertThat(lecture.contentValue).isEqualTo("123123")
    }

    @Test
    fun `LectureContent 정보가 있다면 수정한다`() {
        // given
        val lecture = lecture(contentValue = "before")

        // when
        lecture.setContentValue("after")

        // then
        assertThat(lecture.contentValue).isEqualTo("after")
    }

}
