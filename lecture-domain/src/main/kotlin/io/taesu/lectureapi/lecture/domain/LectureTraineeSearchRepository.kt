package io.taesu.lectureapi.lecture.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

interface LectureTraineeSearchRepository: JpaRepository<LectureTrainee, Long> {
    fun findAll(specification: Specification<LectureTrainee>?, pageable: Pageable): Slice<LectureTrainee>
}
