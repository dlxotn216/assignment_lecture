package io.taesu.lectureapi.lecture.application

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.common.exception.ErrorCode
import io.taesu.lectureapi.helper.*
import io.taesu.lectureapi.lecture.domain.LectureApplyEvent
import io.taesu.lectureapi.lecture.domain.LectureCancelEvent
import io.taesu.lectureapi.lecture.domain.LectureTrainee
import io.taesu.lectureapi.lecture.domain.LectureTraineeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@ExtendWith(MockKExtension::class)
class LectureApplyServiceTest {
    @MockK
    private lateinit var lectureRetrieveService: LectureRetrieveService

    @MockK
    private lateinit var lectureTraineeRepository: LectureTraineeRepository

    @MockK
    private lateinit var eventPublisher: ApplicationEventPublisher

    @SpyK
    @InjectMockKs
    private lateinit var lectureApplyService: LectureApplyService

    @Test
    fun `강연 신청 성공 테스트`() {
        // given
        every { lectureRetrieveService.existsLectureTrainee(LECTURE_KEY, TRAINEE_ID) } returns false

        val requestContext = requestContext()
        val lecture = lecture(LECTURE_KEY, releasedAt = requestContext.nowDateTime.plusDays(1))
        every { lectureRetrieveService.retrieve(LECTURE_KEY) } returns lecture

        val lectureTraineeCapturingSlot = slot<LectureTrainee>()
        every { lectureTraineeRepository.save(capture(lectureTraineeCapturingSlot)) } answers { firstArg() }

        val eventSlot = slot<LectureApplyEvent>()
        every { eventPublisher.publishEvent(capture(eventSlot)) } answers { }

        // when
        lectureApplyService.apply(LECTURE_KEY, TRAINEE_ID, requestContext)

        // then
        lectureTraineeCapturingSlot.captured.run {
            assertThat(lectureKey).isEqualTo(LECTURE_KEY)
            assertThat(traineeId).isEqualTo(TRAINEE_ID)
        }
        eventSlot.captured.run {
            assertThat(this.lectureKey).isEqualTo(LECTURE_KEY)
            assertThat(this.traineeId).isEqualTo(TRAINEE_ID)
            assertThat(this.requestContext).isEqualTo(requestContext)
        }
    }

    @Test
    fun `이미 신청한 강연은 중복 신청할 수 없다`() {
        // given
        every { lectureRetrieveService.existsLectureTrainee(LECTURE_KEY, TRAINEE_ID) } returns true


        // when
        val exception = assertThrows<BusinessException> {
            lectureApplyService.apply(LECTURE_KEY, TRAINEE_ID, requestContext())
        }

        // then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.ALREADY_APPLIED)
    }

    @Test
    fun `강연 신청 취소 성공 테스트`() {
        // given
        val lectureTrainee = lectureTrainee()
        every { lectureRetrieveService.retrieveLectureTrainee(LECTURE_KEY, TRAINEE_ID) } returns lectureTrainee

        val requestContext = requestContext()
        val lecture = lecture(
            LECTURE_KEY,
            releasedAt = requestContext.nowDateTime.plusDays(1),
            maxTraineeCount = 20,
            remainTraineeCount = 19,
        )
        every { lectureRetrieveService.retrieve(LECTURE_KEY) } returns lecture

        val lectureTraineeCapturingSlot = slot<LectureTrainee>()
        every { lectureTraineeRepository.delete(capture(lectureTraineeCapturingSlot)) } answers {  }

        val eventSlot = slot<LectureCancelEvent>()
        every { eventPublisher.publishEvent(capture(eventSlot)) } answers { }

        // when
        lectureApplyService.cancel(LECTURE_KEY, TRAINEE_ID, requestContext)

        // then
        lectureTraineeCapturingSlot.captured.run {
            assertThat(lectureKey).isEqualTo(LECTURE_KEY)
            assertThat(traineeId).isEqualTo(TRAINEE_ID)
        }
        eventSlot.captured.run {
            assertThat(this.lectureKey).isEqualTo(LECTURE_KEY)
            assertThat(this.traineeId).isEqualTo(TRAINEE_ID)
            assertThat(this.requestContext).isEqualTo(requestContext)
        }
    }
}
