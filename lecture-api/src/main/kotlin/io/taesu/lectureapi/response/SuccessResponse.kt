package io.taesu.lectureapi.response

import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.common.i18n.SimpleText
import io.taesu.lectureapi.common.i18n.Translatable

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class SuccessResponse<T>(
    val result: T,
    val requestContext: RequestContext,
    val message: Translatable = SimpleText("요청이 성공했습니다."),
): Response {
    override val status: ResponseStatus = ResponseStatus.SUCCESS
}
