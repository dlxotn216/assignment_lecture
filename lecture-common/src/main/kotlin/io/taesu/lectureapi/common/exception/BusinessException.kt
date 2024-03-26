package io.taesu.lectureapi.common.exception

/**
 * Created by itaesu on 2024/03/21.
 *
 * 서비스 공통 예외
 *
 * @author Lee Tae Su
 */
class BusinessException(
    val errorCode: ErrorCode,
    val messageId: String = errorCode.messageId,
    val messageArgs: Array<Any> = emptyArray(),
    val debugMessage: String,
): RuntimeException(debugMessage)
