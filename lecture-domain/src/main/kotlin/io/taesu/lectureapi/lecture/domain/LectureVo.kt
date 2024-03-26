package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.common.domain.Identifiable
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
data class LectureVo(
    val lectureKey: Long,
    val name: String,
    val maxTraineeCount: Int,
    val remainTraineeCount: Int,
    val releasedAt: LocalDateTime,
): Identifiable<Long> {
    override val key: Long
        get() = lectureKey

    companion object {
        fun from(lecture: Lecture) = LectureVo(
            lectureKey = lecture.lectureKey,
            name = lecture.name,
            maxTraineeCount = lecture.maxTraineeCount,
            remainTraineeCount = lecture.remainTraineeCount,
            releasedAt = lecture.releasedAt,
        )
    }
}
