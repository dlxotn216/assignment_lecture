package io.taesu.lectureapi.lecture.domain

import io.taesu.lectureapi.helper.LECTURE_KEY
import io.taesu.lectureapi.helper.LECTURE_TRAINEE_KEY
import io.taesu.lectureapi.helper.TRAINEE_ID
import io.taesu.lectureapi.helper.lectureTrainee
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Created by itaesu on 2024/03/25.
 *
 * @author Lee Tae Su
 */
class LectureTraineeTest {
    @Test
    fun `Lecture trainee의 일치 여부 테스트`() {
        // given
        val lectureTrainee = lectureTrainee(LECTURE_TRAINEE_KEY, LECTURE_KEY, TRAINEE_ID)

        // when
        val isMatched = lectureTrainee.matches(LECTURE_KEY, TRAINEE_ID)

        // then
        assertThat(isMatched).isTrue()
    }
}
