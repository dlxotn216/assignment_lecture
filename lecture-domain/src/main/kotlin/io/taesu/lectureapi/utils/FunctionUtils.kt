package io.taesu.lectureapi.utils

import io.taesu.lectureapi.common.exception.throwResourceNotFound
import io.taesu.lectureapi.common.vo.LocalDateRange
import io.taesu.lectureapi.common.vo.LocalDateTimeRange
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import kotlin.reflect.KProperty

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */

inline fun <T, S> T?.specIf(block: (T) -> Specification<S>): Specification<S>? {
    if (this == null) return null
    return block(this)
}

infix fun <T: Comparable<T>, E> KProperty<*>.eq(value: T): Specification<E> {
    return Specification { root, _, builder ->
        builder.equal(root.get<T>(this.name), value)
    }
}

infix fun <T: Comparable<T>, E> KProperty<*>.gt(value: T): Specification<E> {
    return Specification { root, _, builder ->
        builder.greaterThan(root.get(this.name), value)
    }
}

infix fun <T: Comparable<T>, E> KProperty<*>.lt(value: T): Specification<E> {
    return Specification { root, _, builder ->
        builder.lessThan(root.get(this.name), value)
    }
}

infix fun <E> KProperty<*>.between(range: LocalDateRange): Specification<E> {
    return Specification { root, _, builder ->
        builder.between(root.get(this.name), range.startDate, range.endDate)
    }
}

infix fun <E> KProperty<*>.between(range: LocalDateTimeRange): Specification<E> {
    return Specification { root, _, builder ->
        builder.between(root.get(this.name), range.startDateTime, range.endDateTime)
    }
}

infix fun <E> KProperty<*>.betweenRangeClosed(range: LocalDateTimeRange): Specification<E> {
    return Specification<E> { root, _, builder ->
        builder.greaterThanOrEqualTo(root.get(this.name), range.startDateTime)
    }.and { root, _, builder ->
        builder.lessThan(root.get(this.name), range.endDateTime)
    }
}

inline fun <reified T, ID> CrudRepository<T, ID>.findOrThrow(id: ID): T {
    return findByIdOrNull(id) ?: throwResourceNotFound(T::class.simpleName ?: T::class.toString(), id.toString())
}
