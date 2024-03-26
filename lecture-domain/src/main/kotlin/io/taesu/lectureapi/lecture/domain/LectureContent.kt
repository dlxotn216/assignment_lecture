package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.common.utils.hash
import jakarta.persistence.*

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@Table(name = "app_lecture_content")
@Entity(name = "LectureContent")
class LectureContent(
    @Id
    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH],
        optional = false
    )
    @JoinColumn(name = "lecture_key", updatable = false)
    val lecture: Lecture,

    @Column(name = "content", nullable = false, columnDefinition = "clob")
    var content: String,
) {
    fun update(contentValue: String) {
        this.content = contentValue
    }

    override fun equals(other: Any?): Boolean =
        this === other || (
            other is LectureContent
                && other.lecture == lecture
            )
    override fun hashCode(): Int = hash(lecture)
}
