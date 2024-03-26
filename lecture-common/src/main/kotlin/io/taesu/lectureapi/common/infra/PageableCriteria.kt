package io.taesu.lectureapi.common.infra

import io.taesu.lectureapi.common.utils.hash

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
open class PageableCriteria(
    page: Int = 1,
    size: Int = 10,
) {
    var page = if (page < 1) 1 else page
        protected set
    var size = if (size > REQUEST_MAX_SIZE) REQUEST_MAX_SIZE else size
        protected set

    override fun equals(other: Any?): Boolean =
        this === other || (
            other is PageableCriteria
                && other.page == this.page
                && other.size == this.size
            )

    override fun hashCode(): Int = hash(page, size)

    companion object {
        const val REQUEST_MAX_SIZE = 100
    }
}
