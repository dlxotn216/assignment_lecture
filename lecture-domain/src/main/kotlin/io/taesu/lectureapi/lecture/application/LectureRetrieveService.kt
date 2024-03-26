package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.lecture.domain.*
import io.taesu.lectureapi.utils.findOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@Service
@Transactional(readOnly = true)
class LectureRetrieveService(
    private val lectureRepository: LectureRepository,
    private val lectureTraineeRepository: LectureTraineeRepository,
) {
    /**
     * @param lectureKey 강의 식별자
     * @return Lecture Entity
     */
    fun retrieve(lectureKey: Long): Lecture = lectureRepository.findOrThrow(lectureKey)

    /**
     * @param lectureKeys 강의 식별자 목록
     * @return LectureVo 목록
     */
    fun retrieveAllVo(lectureKeys: Collection<Long>) =
        lectureRepository.findAllByLectureKeyIn(lectureKeys, LectureVo::class.java)

    /**
     * @param lectureKey 강의 식별자
     * @param traineeId 사번
     * @return 강의 신청 여부
     */
    fun existsLectureTrainee(lectureKey: Long, traineeId: String) =
        lectureTraineeRepository.existsByLectureKeyAndTraineeId(lectureKey, traineeId)

    /**
     * @param lectureKey 강의 식별자
     * @param traineeId 사번
     * @return LectureTraineeEntity
     */
    fun retrieveLectureTrainee(lectureKey: Long, traineeId: String): LectureTrainee =
        lectureTraineeRepository.findOrThrow(lectureKey, traineeId)
}
