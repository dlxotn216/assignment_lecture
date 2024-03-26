package io.taesu.lectureapi.http.lecture.dtos

import io.taesu.lectureapi.common.infra.SliceCriteria

/**
 * Back office에서 전체 강연 목록 조회를 위한 검색 조건
 */
class AllLectureSearchCriteria(
    size: Int = 10,
    lastKey: Long? = null,
): SliceCriteria<Long?>(size, lastKey)

/**
 * Front API에서 신청 가능 강연 목록 조회를 위한 검색 조건
 */
class AvailableLectureSearchCriteria(
    size: Int = 10,
    lastKey: Long? = null,
): SliceCriteria<Long?>(size, lastKey)


/**
 * 나의 강연 목록 조회를 위한 검색 조건
 */
class MyLectureSearchCriteria(
    size: Int = 10,
    val traineeId: String,
    lastKey: Long? = null,
): SliceCriteria<Long?>(size, lastKey) {
}
