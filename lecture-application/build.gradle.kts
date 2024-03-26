plugins {
    `java-test-fixtures`
}

dependencies {
    implementation(project(":lecture-common"))
    implementation(project(":lecture-domain"))
    implementation(project(":lecture-cache"))

    implementation("org.springframework:spring-tx")
    testImplementation(testFixtures(project(":lecture-domain")))
    testImplementation(testFixtures(project(":lecture-cache")))
}
