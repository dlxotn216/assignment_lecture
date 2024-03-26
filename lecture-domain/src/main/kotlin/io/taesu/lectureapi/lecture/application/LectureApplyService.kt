package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.common.exception.throwAlreadyApplied
import io.taesu.lectureapi.lecture.domain.LectureApplyEvent
import io.taesu.lectureapi.lecture.domain.LectureCancelEvent
import io.taesu.lectureapi.lecture.domain.LectureTraineeRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@Service
@Transactional
class LectureApplyService(
    private val lectureRetrieveService: LectureRetrieveService,
    private val lectureTraineeRepository: LectureTraineeRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {
    /**
     * 강의 신청 메서드
     * @param lectureKey Long 강의 식별자
     * @param traineeId String 사번
     */
    fun apply(
        lectureKey: Long,
        traineeId: String,
        requestContext: RequestContext,
    ) {
        if (lectureRetrieveService.existsLectureTrainee(lectureKey, traineeId)) {
            throwAlreadyApplied(lectureKey, traineeId)
        }
        val lecture = lectureRetrieveService.retrieve(lectureKey)
        val lectureTrainee = lecture.apply(traineeId, requestContext.nowDateTime)

        lectureTraineeRepository.save(lectureTrainee)
        eventPublisher.publishEvent(LectureApplyEvent(lectureKey, traineeId, requestContext))
    }

    /**
     * 강의 취소 메서드
     * @param lectureKey Long 강의 식별자
     * @param traineeId String 사번
     */
    fun cancel(
        lectureKey: Long,
        traineeId: String,
        requestContext: RequestContext,
    ) {
        val lectureTrainee = lectureRetrieveService.retrieveLectureTrainee(lectureKey, traineeId)
        val lecture = lectureRetrieveService.retrieve(lectureKey)

        lecture.cancel(traineeId, requestContext.nowDateTime)
        lectureTraineeRepository.delete(lectureTrainee)
        eventPublisher.publishEvent(LectureCancelEvent(lectureKey, traineeId, requestContext))
    }
}
