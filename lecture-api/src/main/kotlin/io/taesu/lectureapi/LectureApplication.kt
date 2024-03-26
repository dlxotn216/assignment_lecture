package io.taesu.lectureapi

import io.taesu.lectureapi.common.context.RequestContext.Companion.DEFAULT_ZONE_ID
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import java.util.*

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@ConfigurationPropertiesScan
@SpringBootApplication
class LectureApplication {
    @PostConstruct
    fun onConstruct() {
        TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_ZONE_ID))
    }
}

fun main(args: Array<String>) {
    runApplication<LectureApplication>(*args)
}
