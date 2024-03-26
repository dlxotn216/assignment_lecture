plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "lecture"
include("lecture-domain")
include("lecture-common")
include("lecture-api")
include("lecture-cache")
include("lecture-application")
