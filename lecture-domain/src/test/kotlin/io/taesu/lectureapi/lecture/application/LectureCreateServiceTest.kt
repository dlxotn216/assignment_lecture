package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.common.exception.ErrorCode
import io.taesu.lectureapi.helper.AbstractDomainIntegrationTest
import io.taesu.lectureapi.helper.lectureCreateCommand
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@Transactional
class LectureCreateServiceTest: AbstractDomainIntegrationTest() {
    @Autowired
    private lateinit var lectureCreateService: LectureCreateService

    @Autowired
    private lateinit var lectureRetrieveService: LectureRetrieveService

    @Test
    fun `Lecture 생성 성공 테스트`() {
        // Given
        val nowDateTime = LocalDateTime.parse("2024-03-22T00:00:00")
        val releasedAt = nowDateTime.plusDays(2)
        val command = lectureCreateCommand(releasedAt)

        // When
        val lectureKey = lectureCreateService.create(command, nowDateTime)

        // Then
        val lecture = lectureRetrieveService.retrieve(lectureKey)
        assertThat(command.name).isEqualTo(lecture.name)
        assertThat(command.location).isEqualTo(lecture.location.place)
        assertThat(command.releasedAt).isEqualTo(lecture.releasedAt)
        assertThat(command.trainer).isEqualTo(lecture.trainer.name)
        assertThat(command.maxTraineeCount).isEqualTo(lecture.maxTraineeCount)
        assertThat(command.content).isEqualTo(lecture.contentValue)
    }

    @Test
    fun `릴리즈 날짜가 지난 강연은 생성할 수 없다`() {
        // Given
        val nowDateTime = LocalDateTime.parse("2024-03-22T00:00:00")
        val releasedAt = nowDateTime.minusDays(2)
        val command = lectureCreateCommand(releasedAt)

        // When
        val exception = assertThrows<BusinessException> {
            lectureCreateService.create(command, nowDateTime)
        }

        // Then
        assertThat(exception.errorCode).isEqualTo(ErrorCode.INVALID_RELEASE_AT)
    }
}
