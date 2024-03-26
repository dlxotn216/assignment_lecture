package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.common.exception.throwResourceNotFound
import org.springframework.data.jpa.repository.JpaRepository

interface LectureTraineeRepository: JpaRepository<LectureTrainee, Long> {
    fun existsByLectureKeyAndTraineeId(lectureKey: Long, traineeId: String): Boolean
    fun findByLectureKeyAndTraineeId(lectureKey: Long, traineeId: String): LectureTrainee?
}

fun LectureTraineeRepository.findOrThrow(lectureKey: Long, traineeId: String): LectureTrainee {
    return findByLectureKeyAndTraineeId(lectureKey, traineeId)
        ?: throwResourceNotFound(
            LectureTrainee::class.simpleName ?: LectureTrainee::class.toString(),
            "lectureKey: $lectureKey, traineeId: $traineeId"
        )
}
