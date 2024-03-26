package io.taesu.lectureapi.application

import io.taesu.lectureapi.application.helper.Cleanup
import io.taesu.lectureapi.cache.config.properties.RedisProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
@ActiveProfiles("rdb-test", "cache-test")
@ExtendWith(SpringExtension::class)
@SpringBootTest
@EnableConfigurationProperties(RedisProperties::class)
abstract class AbstractApplicationIntegrationTest {
    @Autowired
    private lateinit var cleanUp: Cleanup

    @BeforeEach
    fun cleanup() {
        cleanUp.all()
    }
}
