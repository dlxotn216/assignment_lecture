package io.taesu.lectureapi.lecture.domain

import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository: JpaRepository<Lecture, Long> {
    fun <T> findAllByLectureKeyIn(lectureKeys: Collection<Long>, type: Class<T>): List<T>
}
