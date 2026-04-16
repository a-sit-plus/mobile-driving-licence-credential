pluginManagement {
    repositories {
        maven {
            url = uri("https://raw.githubusercontent.com/a-sit-plus/gradle-conventions-plugin/mvn/repo")
            name = "aspConventions"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

if (System.getProperty("regressionTest") == "true") {
    val vckFile = file("../../vck/build.gradle.kts")
    if (vckFile.exists()) {
        logger.warn("Detected VC-K in ${vckFile.absolutePath}.")
        logger.warn("Including VC-K as composite build.")
        includeBuild("../../vck")
    }
}


rootProject.name = "mobile-driving-licence"
include(":mobiledrivinglicence")
