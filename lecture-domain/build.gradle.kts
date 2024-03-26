plugins {
    `java-test-fixtures`
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    implementation(project(":lecture-common"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("com.mysql:mysql-connector-j")

    // testFixtures > MockHelper 의존성
    testFixturesApi("com.navercorp.fixturemonkey:fixture-monkey-starter:1.0.14")
    testFixturesApi("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.0.14")
    testFixturesApi("org.springframework.boot:spring-boot-starter-data-jpa")

}
