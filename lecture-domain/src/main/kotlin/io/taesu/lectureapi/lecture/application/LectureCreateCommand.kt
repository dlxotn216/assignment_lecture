package io.taesu.lectureapi.lecture.application

import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
data class LectureCreateCommand(
    val name: String,
    val location: String,
    val releasedAt: LocalDateTime,
    val trainer: String,
    val maxTraineeCount: Int,
    val content: String,
)
