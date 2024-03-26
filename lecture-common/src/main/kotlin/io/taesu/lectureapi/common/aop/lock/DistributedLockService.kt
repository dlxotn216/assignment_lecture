package io.taesu.lectureapi.common.aop.lock

import org.slf4j.LoggerFactory

/**
 * Created by itaesu on 2024/03/26.
 *
 * 분산 락을 제공하는 서비스 인터페이스
 *
 * @author Lee Tae Su
 */
interface DistributedLockService {
    fun tryJobWithLock(
        lockContext: LockContext,
        job: () -> Any?,
    ): Any?
}
