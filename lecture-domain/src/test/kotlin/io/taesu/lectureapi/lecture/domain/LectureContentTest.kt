package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.helper.lecture
import io.taesu.lectureapi.helper.lectureVo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by itaesu on 2024/03/25.
 *
 * @author Lee Tae Su
 */
class LectureContentTest {
    @Test
    fun `LectureContent update 테스트`() {
        // given
        val lectureContent = LectureContent(lecture(), "content")

        // when
        lectureContent.update("updated content")

        // then
        assertThat(lectureContent.content).isEqualTo("updated content")
    }

}
