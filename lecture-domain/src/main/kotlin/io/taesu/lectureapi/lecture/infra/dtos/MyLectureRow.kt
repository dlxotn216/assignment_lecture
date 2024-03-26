package io.taesu.lectureapi.lecture.infra.dtos

import io.taesu.lectureapi.common.domain.Identifiable
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/23.
 *
 * @author Lee Tae Su
 */
class MyLectureRow(
    val lectureTraineeKey: Long,
    val lectureKey: Long,
    val name: String,
    val applyAt: LocalDateTime
): Identifiable<Long> {
    override val key: Long
        get() = lectureTraineeKey
}
