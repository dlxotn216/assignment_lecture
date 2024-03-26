package io.taesu.lectureapi.application

import io.taesu.lectureapi.common.aop.lock.AcquireDistributeLock
import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.lecture.application.LectureApplyService
import org.springframework.stereotype.Service

/**
 * Created by itaesu on 2024/03/23.
 *
 * 강의 신청/취소 서비스에 분산 락을 적용한 서비스
 *
 * @author Lee Tae Su
 */
@Service
class LectureApplyWithLockService(private val lectureApplyService: LectureApplyService) {
    @AcquireDistributeLock(key = KEY_PATTERN)
    fun apply(
        lectureKey: Long,
        traineeId: String,
        requestContext: RequestContext,
    ) = lectureApplyService.apply(lectureKey, traineeId, requestContext)

    @AcquireDistributeLock(key = KEY_PATTERN)
    fun cancel(
        lectureKey: Long,
        traineeId: String,
        requestContext: RequestContext,
    ) = lectureApplyService.cancel(lectureKey, traineeId, requestContext)

    companion object {
        // lock:lecture:apply:#{lectureKey}
        const val KEY_PATTERN = "'lock:lecture:apply:' + #lectureKey"
    }
}
