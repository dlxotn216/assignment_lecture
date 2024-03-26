package io.taesu.lectureapi.http.lecture.dtos

import io.taesu.lectureapi.lecture.domain.LectureVo
import java.time.LocalDateTime

class PopularLectureRetrieveResponse(
    private val lectureKeyScores: List<Pair<Long, Double>>,
    private val lectures: List<LectureVo>,
) {
    fun resolvePopularLectures(): List<PopularLecture> {
        val popularLectures = lectures.associateBy { it.lectureKey }
        return lectureKeyScores
            .sortedByDescending { it.second }
            .mapNotNull { (lectureKey, score) ->
                val lectureVo = popularLectures[lectureKey]
                lectureVo?.let {
                    PopularLecture(
                        lectureKey = lectureKey,
                        score = score,
                        name = it.name,
                        maxTraineeCount = it.maxTraineeCount,
                        remainTraineeCount = it.remainTraineeCount,
                        releasedAt = it.releasedAt,
                    )
                }
            }
    }
}

data class PopularLecture(
    val lectureKey: Long,
    val score: Double,
    val name: String,
    val maxTraineeCount: Int,
    val remainTraineeCount: Int,
    val releasedAt: LocalDateTime,
)
