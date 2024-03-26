package io.taesu.lectureapi.dtos

import io.taesu.lectureapi.common.domain.Identifiable
import org.springframework.data.domain.Slice

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class SlicedResult<K, T: Identifiable<K>>(
    val contents: List<T>,
    val isFirst: Boolean,
    val isLast: Boolean,
    val size: Int,
    val hasNext: Boolean,
    val isEmpty: Boolean,
) {
    val lastKey: K? = contents.lastOrNull()?.key

    companion object {
        fun <K, T: Identifiable<K>> from(slice: Slice<T>): SlicedResult<K, T> {
            return SlicedResult(
                contents = slice.content,
                isFirst = slice.isFirst,
                isLast = slice.isLast,
                size = slice.size,
                hasNext = slice.hasNext(),
                isEmpty = slice.isEmpty,
            )
        }
    }
}
