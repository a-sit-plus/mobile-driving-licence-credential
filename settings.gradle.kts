pluginManagement {
    repositories {
        maven {
            url = uri("https://raw.githubusercontent.com/a-sit-plus/gradle-conventions-plugin/mvn/repo")
            name = "aspConventions"
        }
        mavenCentral()
        gradlePluginPortal()
    }

    // The composite build is only active when regression testing AND vck is present on disk.
    // pluginManagement {} is compiled in an isolated scope (it can't see script-level
    // functions/vals), so we decide here — it always runs before the body — and stash the
    // result in a system property the body reads back, keeping the decision in one place.
    val compositeBuild = System.getProperty("regressionTest") == "true"
            && file("../../vck/build.gradle.kts").exists()
    System.setProperty("vck.compositeBuild", compositeBuild.toString())

    // When the composite build IS active, vck contributes the conventions plugin from source
    // at an "unknown version" and requesting any version would clash, so we skip the version.
    // Otherwise the plugin must be resolved from the repository, so we supply its version here.
    if (!compositeBuild) {
        val aspVersion = file("gradle/libs.versions.toml").readLines()
            .first { it.substringBefore("=").trim() == "asp" }
            .substringAfter('"').substringBefore('"')
        plugins {
            id("at.asitplus.gradle.conventions") version aspVersion
        }
    }
}

if (System.getProperty("vck.compositeBuild") == "true") {
    val vckFile = file("../../vck/build.gradle.kts")
    logger.warn("Detected VC-K in ${vckFile.absolutePath}.")
    logger.warn("Including VC-K as composite build.")
    includeBuild("../../vck")
}


rootProject.name = "mobile-driving-licence"
include(":mobiledrivinglicence")
