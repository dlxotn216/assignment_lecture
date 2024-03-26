package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.base.domain.BaseEntity
import io.taesu.lectureapi.common.utils.hash
import jakarta.persistence.*

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@Table(name = "app_lecture_trainee")
@Entity(name = "LectureTrainee")
class LectureTrainee(
    @Id
    @Column(name = "lecture_trainee_key", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val lectureTraineeKey: Long = 0L,

    @Column(name = "lecture_key", nullable = false)
    val lectureKey: Long,

    @Column(name = "trainee_id", length = 5, nullable = false)
    val traineeId: String,
): BaseEntity<Long>() {
    override fun getId(): Long = lectureTraineeKey

    fun matches(lectureKey: Long, traineeId: String): Boolean =
        this.lectureKey == lectureKey && this.traineeId == traineeId

    override fun equals(other: Any?): Boolean =
        this === other || (
            other is LectureTrainee
                && other.lectureKey == lectureKey
                && other.traineeId == traineeId
            )

    override fun hashCode(): Int = hash(lectureKey, traineeId)
}

