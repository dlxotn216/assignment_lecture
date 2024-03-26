package io.taesu.lectureapi.helper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@ActiveProfiles("rdb-test")
@ExtendWith(SpringExtension::class)
@SpringBootTest
abstract class AbstractDomainIntegrationTest {
    @Autowired
    private lateinit var cleanUp: CleanupDatabase

    @BeforeEach
    fun cleanup() {
        cleanUp.all()
    }
}
