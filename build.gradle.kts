plugins {
    id("at.asitplus.gradle.conventions") version "20250628"
    id("io.kotest.multiplatform") version libs.versions.kotest
    kotlin("multiplatform") version libs.versions.kotlin apply false
    kotlin("plugin.serialization") version libs.versions.kotlin apply false
}

val artifactVersion: String by extra
group = "at.asitplus.wallet"
version = artifactVersion
