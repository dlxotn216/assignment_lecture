tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":lecture-common"))
    implementation(project(":lecture-application"))
    implementation(project(":lecture-domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation(testFixtures(project(":lecture-domain")))
}
