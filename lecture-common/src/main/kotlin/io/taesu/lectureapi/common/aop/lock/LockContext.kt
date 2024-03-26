package io.taesu.lectureapi.common.aop.lock

import java.util.concurrent.TimeUnit

data class LockContext(
    val key: String,
    val waitTime: Long = 5L,
    val lockTimeout: Long = 5L,
    val timeUnit: TimeUnit = TimeUnit.SECONDS,
)
