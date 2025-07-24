import at.asitplus.gradle.Logger
import at.asitplus.gradle.setupDokka

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("at.asitplus.gradle.conventions")
    id("org.jetbrains.dokka")
    id("signing")
}

/* required for maven publication */
val artifactVersion: String by extra
group = "at.asitplus.wallet"
version = artifactVersion

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                api(libs.vck)
            }
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

repositories {
    mavenCentral()
    maven(url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
}

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
}

