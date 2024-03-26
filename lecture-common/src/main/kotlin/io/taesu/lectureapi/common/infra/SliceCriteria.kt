package io.taesu.lectureapi.common.infra

open class SliceCriteria<K>(
    size: Int = 10,
    val lastKey: K,
) {
    val size: Int = if (size > PageableCriteria.REQUEST_MAX_SIZE) PageableCriteria.REQUEST_MAX_SIZE else size
}
