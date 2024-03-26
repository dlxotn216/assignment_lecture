package io.taesu.lectureapi.common.aop.lock

import java.util.concurrent.TimeUnit


/**
 * Created by itaesu on 2024/03/22.
 *
 * 분산 락을 취득하기 위한 어노테이션
 *
 * @author Lee Tae Su
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AcquireDistributeLock(
    val key: String,    // SPEL 기반의 키 표현식

    val waitTime: Long = 5L,

    val lockTimeout: Long = 5L,

    val timeUnit: TimeUnit = TimeUnit.SECONDS,
)
