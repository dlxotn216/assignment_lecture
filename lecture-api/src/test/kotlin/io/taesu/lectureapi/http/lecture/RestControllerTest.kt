package io.taesu.lectureapi.http.lecture

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.taesu.lectureapi.application.LectureApplyWithLockService
import io.taesu.lectureapi.application.PopularLectureService
import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.common.context.RequestContext.Companion.ZONE_ID_HEADER
import io.taesu.lectureapi.common.i18n.MessageService
import io.taesu.lectureapi.http.TestExceptionController
import io.taesu.lectureapi.http.config.properties.CorsFilterProperty
import io.taesu.lectureapi.lecture.application.*
import io.taesu.lectureapi.response.SuccessResponse
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@WebMvcTest(
    controllers = [
        MessageService::class,
        CorsFilterProperty::class,
        TestExceptionController::class,
        AvailableLectureSearchController::class,
        AllLectureSearchController::class,
        LectureApplyController::class,
        LectureCreateController::class,
        LectureTraineeSearchController::class,
        MyLectureSearchController::class,
        PopularLectureRetrieveController::class,
    ]
)
open class RestControllerTest {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @MockkBean
    protected lateinit var lectureSearchService: LectureSearchService

    @MockkBean
    lateinit var lectureApplyWithLockService: LectureApplyWithLockService

    @MockkBean
    lateinit var lectureCreateService: LectureCreateService

    @MockkBean
    lateinit var lectureTraineeSearchService: LectureTraineeSearchService

    @MockkBean
    lateinit var myLectureSearchService: MyLectureSearchService

    @MockkBean
    lateinit var popularLectureService: PopularLectureService

    @MockkBean
    lateinit var lectureRetrieveService: LectureRetrieveService

    protected fun setHeader(
        builder: MockHttpServletRequestBuilder,
        authorization: String = "Bearer token q92305tq3",
        acceptLang: String = "ko",
        zoneId: String = "Asia/Seoul",
        nowDateTime: String = "2024-03-24T12:00:00",
        contentType: String = MediaType.APPLICATION_JSON_VALUE,
        accept: String = MediaType.APPLICATION_JSON_VALUE,
    ): MockHttpServletRequestBuilder {
        return builder
            .header("Authorization", authorization)
            .header("Accept-Language", acceptLang)
            .header(ZONE_ID_HEADER, zoneId)
            .header("X-NOW_DATETIME", nowDateTime)
            .contentType(contentType)
            .accept(accept)
    }

    protected fun jsonBody(body: Any?): String {
        return objectMapper.writeValueAsString(body)
    }

    protected fun successResponse(expected: Any?, requestContext: RequestContext): String {
        return objectMapper.writeValueAsString(SuccessResponse(expected, requestContext))
    }
}
