import at.asitplus.gradle.Logger
import at.asitplus.gradle.kotest
import at.asitplus.gradle.serialization
import at.asitplus.gradle.setupDokka
import at.asitplus.gradle.tbAddons

plugins {
    // No version here: in composite mode vck contributes this plugin from source at an
    // "unknown version"; the version is supplied by settings.gradle.kts otherwise.
    id("at.asitplus.gradle.conventions")
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    id("org.jetbrains.dokka")
    // maven-publish is applied transitively by the conventions plugin, but when that
    // plugin is contributed by the vck composite build (at an "unknown version") Gradle
    // can't introspect its transitive plugins to generate the `publishing` accessor.
    // Listing it here makes accessor generation work in both composite and binary modes.
    id("maven-publish")
    id("signing")
    alias(libs.plugins.testballoon)
}

/* required for maven publication */
val artifactVersion: String by extra
group = "at.asitplus.wallet"
version = artifactVersion

kotlin {
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                api(libs.vck)
            }
        }
        commonTest.dependencies {
            implementation(tbAddons("matrix")) //work around idea bug
        }
    }
}

val javadocJar = setupDokka(baseUrl = "https://github.com/a-sit-plus/mobile-driving-licence/tree/main/")

//catch the missing `signMavenPublication` Task, which slips through for reasons unknown
afterEvaluate {
    val signTasks = tasks.filter { it.name.startsWith("sign") }
    tasks.filter { it.name.startsWith("publish") }.forEach {
        Logger.lifecycle("   * ${it.name} now depends on ${signTasks.joinToString { it.name }}")
        it.dependsOn(*signTasks.toTypedArray())
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set("Mobile Driving Licence")
                description.set("Use data representing Mobile Driving Licences as a ISO 18013-5 Credential")
                url.set("https://github.com/a-sit-plus/mobile-driving-licence/")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("nodh")
                        name.set("Christian Kollmann")
                        email.set("christian.kollmann@a-sit.at")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:a-sit-plus/mobile-driving-licence.git")
                    developerConnection.set("scm:git:git@github.com:a-sit-plus/mobile-driving-licence.git")
                    url.set("https://github.com/a-sit-plus/mobile-driving-licence/")
                }
            }
        }
    }
    repositories {
        mavenLocal {
            signing.isRequired = false
        }
        maven {
            url = uri(rootProject.layout.projectDirectory.dir("..").dir("repo"))
            name = "local"
            signing.isRequired = false
        }
    }
}

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
}

