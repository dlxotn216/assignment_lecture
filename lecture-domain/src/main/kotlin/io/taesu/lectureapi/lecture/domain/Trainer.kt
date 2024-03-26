package io.taesu.lectureapi.lecture.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@Embeddable
data class Trainer(
    @Column(name = "trainer_name", length = 256, nullable = false)
    val name: String,
)
