package io.taesu.lectureapi.helper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.common.i18n.SupportLang
import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.lecture.application.LectureCreateCommand
import io.taesu.lectureapi.lecture.domain.*
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
val fixtureMonkey: FixtureMonkey = FixtureMonkey.builder()
    .plugin(KotlinPlugin())
    .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
    .build()

inline fun <reified T> giveMeOne(): T {
    while (true) {
        try {
            return fixtureMonkey.giveMeOne(T::class.java)
        } catch (e: Exception) {
            // ignore
        }
    }
}

fun lecture(
    lectureKey: Long = LECTURE_KEY,
    name: String = "name",
    releasedAt: LocalDateTime = LocalDateTime.now(),
    maxTraineeCount: Int = 20,
    remainTraineeCount: Int = maxTraineeCount,
    contentValue: String? = null,
) = Lecture(
    lectureKey = lectureKey,
    name = name,
    location = Location("Seoul"),
    releasedAt = releasedAt,
    trainer = Trainer("Lee Tae Su"),
    maxTraineeCount = maxTraineeCount,
    remainTraineeCount = remainTraineeCount,
).apply {
    if (contentValue != null) {
        setContentValue(contentValue)
    }
}

fun fixedReleasedAtLectures(): List<Lecture> {
    return listOf(
        lecture(0L, "lecture 1", LocalDateTime.parse("2024-03-22T00:00:00")),
        lecture(0L, "lecture 2", LocalDateTime.parse("2024-03-22T23:59:59")),
        lecture(0L, "lecture 3", LocalDateTime.parse("2024-03-23T00:00:00")),
        lecture(0L, "lecture 4", LocalDateTime.parse("2024-03-30T23:59:59")),
        lecture(0L, "lecture 5", LocalDateTime.parse("2024-03-31T00:00:00")),
    )
}

fun lectures(size: Int): List<Lecture> {
    return (1..size).map {
        lecture(0L, "lecture $it", LocalDateTime.now())
    }
}

fun lectureVo(lectureKey: Long): LectureVo {
    while (true) {
        try {
            return fixtureMonkey.giveMeBuilder<LectureVo>()
                .setExp(LectureVo::lectureKey, lectureKey)
                .sample()
        } catch (e: Exception) {
            // ignore
        }
    }
}

fun lectureTrainee(
    lectureTraineeKey: Long = LECTURE_TRAINEE_KEY,
    lectureKey: Long = LECTURE_KEY,
    traineeId: String = TRAINEE_ID,
) = LectureTrainee(lectureTraineeKey, lectureKey, traineeId)

fun lectureCreateCommand(releasedAt: LocalDateTime): LectureCreateCommand {
    while (true) {
        try {
            return fixtureMonkey.giveMeBuilder<LectureCreateCommand>()
                .setExp(LectureCreateCommand::releasedAt, releasedAt)
                .sample()
        } catch (e: Exception) {
            // ignore
        }
    }
}

fun lectureApplyEvent() = LectureApplyEvent(LECTURE_KEY, "00001", requestContext())
fun lectureCancelEvent() = LectureCancelEvent(LECTURE_KEY, "00001", requestContext())

fun requestContextWithReserved() = requestContext(
    supportLang = SupportLang.KOREAN,
    zoneId = ZoneId.of("Asia/Seoul"),
    nowDateTime = LocalDateTime.parse("2024-03-24T12:00:00")
)

fun requestContext(
    supportLang: SupportLang = SupportLang.KOREAN,
    zoneId: ZoneId = ZoneId.of("Asia/Seoul"),
    nowDateTime: LocalDateTime = LocalDateTime.now(),
) = RequestContext(
    supportLang = supportLang,
    zoneId = zoneId,
    nowDateTime = nowDateTime
)

fun lectureVoSlicedResult(): SlicedResult<Long, LectureVo> {
    val slicedResult = SlicedResult(
        contents = listOf(lectureVo(6L), lectureVo(5L), lectureVo(4L)),
        isFirst = false,
        isLast = false,
        size = 3,
        hasNext = true,
        isEmpty = false
    )
    return slicedResult
}
