plugins {
    `java-test-fixtures`
}

dependencies {
    implementation(project(":lecture-common"))
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.redisson:redisson-spring-boot-starter:3.27.2")

    testFixturesApi("org.springframework.boot:spring-boot-starter-data-redis")
}
