package io.taesu.lectureapi.lecture.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@Embeddable
data class Location(
    @Column(name = "location", length = 512, nullable = false)
    val place: String,
)
