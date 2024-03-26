package io.taesu.lectureapi.cache.infra.impl

import io.taesu.lectureapi.common.aop.lock.DistributedLockService
import io.taesu.lectureapi.common.aop.lock.LockContext
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


/**
 * Created by itaesu on 2024/03/22.
 *
 * Redisson을 이용한 분산 락 서비스 구현체
 *
 * @author Lee Tae Su
 */
@Service
class RedissonDistributedLockService(private val redissonClient: RedissonClient): DistributedLockService {
    override fun tryJobWithLock(
        lockContext: LockContext,
        job: () -> Any?,
    ): Any? {
        val key = "lock:${lockContext.key}"
        log.info("AcquireDistributeLock [{}]", key)
        val rLock = redissonClient.getLock(key)

        try {
            val succeed = rLock.tryLock(lockContext.waitTime, lockContext.lockTimeout, lockContext.timeUnit)
            log.info("Success to AcquireDistributeLock [{}]", key)
            if (succeed) {
                return job()
            }

            return false
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            tryUnlock(rLock, lockContext)
        }
    }

    private fun tryUnlock(rLock: RLock, lock: LockContext) {
        try {
            rLock.unlock()
        } catch (e: IllegalMonitorStateException) {
            log.info("Fail to unlock [{}]", lock.key)
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}

