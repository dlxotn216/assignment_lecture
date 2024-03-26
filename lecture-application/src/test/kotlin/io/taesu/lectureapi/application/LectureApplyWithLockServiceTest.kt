package io.taesu.lectureapi.application

import io.taesu.lectureapi.helper.requestContext
import io.taesu.lectureapi.lecture.application.LectureCreateCommand
import io.taesu.lectureapi.lecture.application.LectureCreateService
import io.taesu.lectureapi.lecture.domain.LectureRepository
import io.taesu.lectureapi.lecture.domain.LectureTraineeRepository
import io.taesu.lectureapi.utils.findOrThrow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random


/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class LectureApplyWithLockServiceTest: AbstractApplicationIntegrationTest() {
    private val maxTraineeCount = 10
    private val numberOfThreads = 100

    @Autowired
    private lateinit var lectureRepository: LectureRepository

    @Autowired
    private lateinit var lectureTraineeRepository: LectureTraineeRepository

    @Autowired
    private lateinit var lectureCreateService: LectureCreateService

    @Autowired
    private lateinit var lectureApplyWithLockService: LectureApplyWithLockService

    @Test
    fun `강연 신청 동시성 테스트`() {
        // given
        val requestContext = requestContext()
        val nowDateTime = requestContext.nowDateTime
        val lectureKey = createLecture(nowDateTime)

        // when
        val executorService = getExecutorService()
        val latch = getLatch()

        repeat(numberOfThreads) {
            executorService.submit {
                try {
                    lectureApplyWithLockService.apply(
                        lectureKey,
                        Random.nextInt(1, 99999).toString(),
                        requestContext
                    )
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        lectureRepository.findOrThrow(lectureKey).let {
            assertThat(it.remainTraineeCount).isEqualTo(0)
        }
        lectureTraineeRepository.findAll().let {
            assertThat(it).hasSize(10)
        }
    }

    @Test
    fun `강연 신청 취소 동시성 테스트`() {
        // given
        val requestContext = requestContext()
        val nowDateTime = requestContext.nowDateTime
        val maxTraineeCount = 10
        val lectureKey = createLecture(nowDateTime)
        val traineeIds = (1..maxTraineeCount).map { Random.nextInt(1, 99999).toString() }
        traineeIds.forEach {
            lectureApplyWithLockService.apply(
                lectureKey,
                it,
                requestContext
            )
        }

        // when
        val executorService = getExecutorService()
        val latch = getLatch()

        repeat(numberOfThreads) {
            val traineeId = traineeIds[it % maxTraineeCount]
            executorService.submit {
                try {
                    lectureApplyWithLockService.cancel(
                        lectureKey,
                        traineeId,
                        requestContext
                    )
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        lectureRepository.findOrThrow(lectureKey).let {
            assertThat(it.remainTraineeCount).isEqualTo(maxTraineeCount)
        }
        lectureTraineeRepository.findAll().let {
            assertThat(it).hasSize(0)
        }
    }

    @Test
    fun `강연 신청_취소 동시성 테스트`() {
        // given
        val requestContext = requestContext()
        val nowDateTime = requestContext.nowDateTime
        val maxTraineeCount = 10
        val lectureKey = createLecture(nowDateTime)
        val traineeIds = (1..maxTraineeCount).map { Random.nextInt(1, 99999).toString() }

        // when
        val futures = traineeIds.map { trainerId ->
            CompletableFuture.supplyAsync {
                    lectureApplyWithLockService.apply(
                        lectureKey,
                        trainerId,
                        requestContext
                    )
                }.thenApply {
                    lectureApplyWithLockService.cancel(
                        lectureKey,
                        trainerId,
                        requestContext
                    )
                }
        }
        futures.forEach {
            it.join()
        }

        // then
        lectureRepository.findOrThrow(lectureKey).let {
            assertThat(it.remainTraineeCount).isEqualTo(maxTraineeCount)
        }
        lectureTraineeRepository.findAll().let {
            assertThat(it).hasSize(0)
        }
    }

    private fun createLecture(nowDateTime: LocalDateTime): Long {
        val lectureKey = lectureCreateService.create(
            LectureCreateCommand(
                name = "Spring Boot 2.0",
                location = "강남역 1번출구",
                releasedAt = nowDateTime.plusDays(4),
                trainer = "Lee Tae Su",
                maxTraineeCount = maxTraineeCount,
                content = "Spring Boot 2.0 실전"
            ),
            nowDateTime
        )
        return lectureKey
    }

    private fun getExecutorService(): ExecutorService = Executors.newFixedThreadPool(numberOfThreads)

    private fun getLatch(): CountDownLatch = CountDownLatch(numberOfThreads)
}
