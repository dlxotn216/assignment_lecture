package io.taesu.lectureapi.http.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@ConfigurationProperties("config.cors")
@ConfigurationPropertiesScan
class CorsFilterProperty @ConstructorBinding constructor(
    val allowedOrigins: List<String>,
)

