package io.taesu.lectureapi.application.listener

import io.taesu.lectureapi.application.PopularLectureService
import io.taesu.lectureapi.lecture.domain.LectureApplyEvent
import io.taesu.lectureapi.lecture.domain.LectureCancelEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

/**
 * Created by itaesu on 2024/03/23.
 *
 * @author Lee Tae Su
 */
@Component
class LectureApplyEventListener(private val popularLectureService: PopularLectureService) {
    /**
     * 강의 신청 이벤트 발생 시 인기 강의 순위를 증가시킨다
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(lectureApplyEvent: LectureApplyEvent) {
        popularLectureService.increase(
            lectureKey = lectureApplyEvent.lectureKey,
            requestContext = lectureApplyEvent.requestContext
        )
    }

    /**
     * 강의 취소 이벤트 발생 시 인기 강의 순위를 감소시킨다
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(lectureCancelEvent: LectureCancelEvent) {
        popularLectureService.decrease(
            lectureKey = lectureCancelEvent.lectureKey,
            requestContext = lectureCancelEvent.requestContext
        )
    }
}
