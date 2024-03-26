package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.base.domain.BaseEntity
import io.taesu.lectureapi.common.exception.throwLectureClosed
import io.taesu.lectureapi.common.exception.throwLectureStateIsFull
import io.taesu.lectureapi.common.exception.throwUnexpected
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.Objects.hash

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@EntityListeners(value = [AuditingEntityListener::class])
@Table(name = "app_lecture")
@Entity(name = "Lecture")
class Lecture(
    @Id
    @Column(name = "lecture_key", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val lectureKey: Long = 0L,

    @Column(name = "name", length = 256, nullable = false)
    val name: String,

    @Embedded
    val location: Location,

    @Column(name = "released_At", nullable = false)
    val releasedAt: LocalDateTime,

    @Embedded
    val trainer: Trainer,

    @Column(name = "max_trainee_count", nullable = false)
    val maxTraineeCount: Int,

    @Column(name = "remain_trainee_count", nullable = false)
    var remainTraineeCount: Int = maxTraineeCount,
): BaseEntity<Long>() {
    override fun getId(): Long = lectureKey

    @OneToMany(
        fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],
        mappedBy = "lecture"
    )
    private val _contents: MutableSet<LectureContent> = mutableSetOf()
    private val content get(): LectureContent? = _contents.firstOrNull()
    val contentValue get(): String = content?.content ?: ""

    fun setContentValue(contentValue: String) {
        val content = this.content
        if (content == null) {
            this._contents += LectureContent(this, contentValue)
            return
        }
        content.update(contentValue)
    }

    fun apply(traineeId: String, nowDateTime: LocalDateTime): LectureTrainee {
        if (nowDateTime.isAfter(releasedAt)) {
            throwLectureClosed(lectureKey)
        }

        if (remainTraineeCount <= 0) {
            throwLectureStateIsFull(lectureKey, traineeId)
        }
        this.decreaseRemainTraineeCount()
        return LectureTrainee(lectureKey = lectureKey, traineeId = traineeId)
    }

    private fun decreaseRemainTraineeCount() {
        this.remainTraineeCount--
    }

    fun cancel(traineeId: String, nowDateTime: LocalDateTime) {
        if (nowDateTime.isAfter(releasedAt)) {
            throwLectureClosed(lectureKey)
        }

        if (remainTraineeCount >= this.maxTraineeCount) {
            throwUnexpected(
                """
                강연을 취소할 수 없는 비정상 상태입니다.
                Lecture [$lectureKey], Requested trainee [$traineeId]
                - remainTraineeCount: $remainTraineeCount
                - maxTraineeCount: $maxTraineeCount
            """.trimIndent()
            )
        }

        this.increaseRemainTraineeCount()
    }

    private fun increaseRemainTraineeCount() {
        this.remainTraineeCount++
    }

    override fun equals(other: Any?): Boolean =
        this === other || (
            other is Lecture
                && other.lectureKey == lectureKey
            )

    override fun hashCode(): Int = hash(lectureKey)
}
