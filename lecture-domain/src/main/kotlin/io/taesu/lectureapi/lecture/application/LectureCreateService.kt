package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.common.exception.throwInvalidReleasedAt
import io.taesu.lectureapi.lecture.domain.Location
import io.taesu.lectureapi.lecture.domain.Trainer
import io.taesu.lectureapi.lecture.domain.Lecture
import io.taesu.lectureapi.lecture.domain.LectureRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@Service
@Transactional
class LectureCreateService(private val lectureRepository: LectureRepository) {
    /**
     * 강의 생성 메서드
     * @param command 강의 생성을 위한 커맨드
     */
    fun create(command: LectureCreateCommand, nowDateTime: LocalDateTime): Long {
        if (command.releasedAt.isBefore(nowDateTime)) {
            throwInvalidReleasedAt(command.releasedAt)
        }
        return lectureRepository.save(command.toLectureEntity()).lectureKey
    }
}

fun LectureCreateCommand.toLectureEntity() = Lecture(
    name = this.name,
    location = Location(this.location),
    releasedAt = this.releasedAt,
    trainer = Trainer(this.trainer),
    maxTraineeCount = this.maxTraineeCount,
).also {
    it.setContentValue(this.content)
}
