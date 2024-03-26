package io.taesu.lectureapi.common.domain

/**
 * Created by itaesu on 2024/03/24.
 *
 * 식별 가능한 객체를 표현하기 위한 인터페이스
 *
 * @author Lee Tae Su
 */
interface Identifiable<T> {
    val key: T
}
