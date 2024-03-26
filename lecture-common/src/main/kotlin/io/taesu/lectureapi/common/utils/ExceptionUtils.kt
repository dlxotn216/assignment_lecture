package io.taesu.lectureapi.common.utils

import io.taesu.lectureapi.common.exception.BusinessException

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
object ExceptionUtils {

    /**
     * @param throwable Throwable
     * @return 로그 등을 위한 디버깅용 메시지.
     * 디버깅 관련 로직이 영향을 미치지 않도록 방어적으로 try-catch 처리
     */
    fun resolveDebugMessage(throwable: Throwable?): String? {
        if (throwable == null) {
            return ""
        }
        try {
            val rootCause = resolveRootCause(throwable)
            return if (rootCause is BusinessException) {
                (rootCause as? BusinessException)?.debugMessage
            } else {
                throwable.message
            }
        } catch (e: Exception) {
            return throwable.message
        }
    }

    /**
     * @param throwable Throwable
     * @return Throwable의 rootCause
     */
    fun resolveRootCause(throwable: Throwable?): Throwable? {
        if (throwable == null) {
            return null
        }

        var rootCause: Throwable? = throwable
        while (rootCause?.cause != null && rootCause.cause !== rootCause) {
            rootCause = rootCause.cause
        }
        return rootCause
    }
}

