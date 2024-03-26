package io.taesu.lectureapi.common.exception

import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
fun throwUnexpected(debugMessage: String): Nothing {
    throw BusinessException(
        errorCode = ErrorCode.UNEXPECTED,
        debugMessage = debugMessage
    )
}

fun throwResourceNotFound(entityName: String, identifier: String): Nothing {
    throw BusinessException(
        errorCode = ErrorCode.RESOURCE_NOT_FOUND,
        debugMessage = "[$entityName][$identifier]"
    )
}

fun throwInvalidReleasedAt(releasedAt: LocalDateTime): Nothing {
    throw BusinessException(
        errorCode = ErrorCode.INVALID_RELEASE_AT,
        debugMessage = "Invalid releaseAt [$releasedAt]"
    )
}

fun throwAlreadyApplied(lectureKey: Long, traineeId: String): Nothing {
    throw BusinessException(
        errorCode = ErrorCode.ALREADY_APPLIED,
        debugMessage = "Lecture [$lectureKey] already applied by trainee [$traineeId]"
    )
}

fun throwLectureClosed(lectureKey: Long): Nothing {
    throw BusinessException(
        errorCode = ErrorCode.LECTURE_CLOSED,
        debugMessage = "Lecture [$lectureKey] closed"
    )
}

fun throwLectureStateIsFull(lectureKey: Long, traineeId: String): Nothing {
    throw BusinessException(
        errorCode = ErrorCode.STATE_IS_FULL,
        debugMessage = "Lecture [$lectureKey] is full for trainee [$traineeId]"
    )
}
