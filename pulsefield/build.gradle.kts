plugins {
    kotlin("jvm")
    id("maven-publish")
    id("jacoco")
    id("org.sonarqube") version "4.3.1.3277"
}

group = "com.github.saizad"
version = project.findProperty("version") ?: "1.0.0"

kotlin {
    jvmToolchain(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withSourcesJar()
    withJavadocJar()
}

jacoco {
    toolVersion = "0.8.10"
}

val mainClassFiles = fileTree("$buildDir/classes/kotlin/main") {
    exclude(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*"
    )
}


tasks.register<JacocoReport>("jacocoTestReport1") {
    dependsOn("test")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(files(mainClassFiles))
    sourceDirectories.setFrom(files("src/main/kotlin", "src/main/java"))
    executionData.setFrom(fileTree(buildDir) {
        include("jacoco/test.exec")
    })

    doFirst {
        println("Classes found for coverage: ${mainClassFiles.files}")
    }
}



tasks.named("sonar") {
    dependsOn("jacocoTestReport1")
}

sonarqube {
    properties {
        property("sonar.projectKey", "Pulse-Field")
        property("sonar.projectName", "Pulse Field")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/jacoco/jacocoTestReport1/jacocoTestReport1.xml")
    }
}


dependencies {
    testImplementation(libs.junit)
    implementation(libs.kotlinx.coroutines.core)

    // **Testing Dependencies**
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test.junit5)

    // **Coroutine & Reactive Testing**
    testImplementation(libs.kotlinx.coroutines.test)

}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "pulsefield"
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("PulseField")
                description.set("Form field validation library")
                url.set("https://github.com/saizad/PulseField")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("saizad")
                        name.set("Your Name")
                        email.set("your.email@example.com")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "local"
            url = uri("${buildDir}/repo")
        }
    }
}

tasks.withType<Test> {
    if (System.getenv("JITPACK") == "true") {
        enabled = false
    }
}