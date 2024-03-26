package io.taesu.lectureapi.lecture.infra

import io.taesu.lectureapi.lecture.domain.Lecture
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
interface LectureSearchRepository: JpaRepository<Lecture, Long> {
    fun findAll(specification: Specification<Lecture>?, pageable: Pageable): Slice<Lecture>

    fun <T> findAllByIdIn(ids: Collection<Long>, type: Class<T>): List<T>
}
