package io.taesu.lectureapi.common.exception

/**
 * Created by itaesu on 2024/03/21.
 *
 * 서비스 공통 에러코드
 *
 * @author Lee Tae Su
 */
enum class ErrorCode {
    UNEXPECTED,
    RESOURCE_NOT_FOUND,
    INVALID_REQUEST,
    INVALID_RELEASE_AT,
    ALREADY_APPLIED,
    STATE_IS_FULL,
    LECTURE_CLOSED,
    ;

    val messageId = "EXCEPTION.$name"
}
