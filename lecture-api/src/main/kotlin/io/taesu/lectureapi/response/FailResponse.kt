package io.taesu.lectureapi.response

import io.taesu.lectureapi.common.i18n.Translatable

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class FailResponse(
    val errorInfo: ErrorInfo,
    val message: String
): Response {
    override val status: ResponseStatus = ResponseStatus.FAIL
}
