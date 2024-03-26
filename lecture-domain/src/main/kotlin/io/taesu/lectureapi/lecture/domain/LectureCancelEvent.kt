package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.common.context.RequestContext

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
data class LectureCancelEvent(
    val lectureKey: Long,
    val traineeId: String,
    val requestContext: RequestContext,
)
