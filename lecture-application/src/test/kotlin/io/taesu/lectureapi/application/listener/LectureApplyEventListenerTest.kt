package io.taesu.lectureapi.application.listener

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.taesu.lectureapi.application.PopularLectureService
import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.helper.lectureApplyEvent
import io.taesu.lectureapi.helper.lectureCancelEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
@ExtendWith(MockKExtension::class)
class LectureApplyEventListenerTest {
    @MockK
    private lateinit var popularLectureService: PopularLectureService

    @SpyK
    @InjectMockKs
    private lateinit var listener: LectureApplyEventListener

    @Test
    fun `강연이 신청되면 실시간 인기 강연 집계를 증가한다`() {
        // given
        val lectureApplyEvent = lectureApplyEvent()
        val lectureKeySlot = slot<Long>()
        val requestContextCapturingSlot = slot<RequestContext>()
        every {
            popularLectureService.increase(
                capture(lectureKeySlot),
                capture(requestContextCapturingSlot)
            )
        } returns Unit

        // when
        listener.handle(lectureApplyEvent)

        // then
        assertThat(lectureKeySlot.captured).isEqualTo(lectureApplyEvent.lectureKey)
        assertThat(requestContextCapturingSlot.captured).isEqualTo(lectureApplyEvent.requestContext)
    }

    @Test
    fun `강연이 신청 취소 되면 실시간 인기 강연 집계를 감소한다`() {
        // given
        val lectureApplyEvent = lectureCancelEvent()
        val lectureKeySlot = slot<Long>()
        val requestContextCapturingSlot = slot<RequestContext>()
        every {
            popularLectureService.decrease(
                capture(lectureKeySlot),
                capture(requestContextCapturingSlot)
            )
        } returns Unit

        // when
        listener.handle(lectureApplyEvent)

        // then
        assertThat(lectureKeySlot.captured).isEqualTo(lectureApplyEvent.lectureKey)
        assertThat(requestContextCapturingSlot.captured).isEqualTo(lectureApplyEvent.requestContext)
    }
}
