package io.taesu.lectureapi.application.helper

import io.taesu.lectureapi.cache.helper.CleanupCache
import io.taesu.lectureapi.helper.CleanupDatabase
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
@Component
class Cleanup(
    private val cleanupDatabase: CleanupDatabase,
    private val cleanupCache: CleanupCache,
) {
    fun all() {
        cleanupDatabase.all()
        cleanupCache.all()
    }
}
