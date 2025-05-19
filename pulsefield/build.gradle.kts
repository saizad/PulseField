plugins {
    kotlin("jvm")
    id("maven-publish")
}

// Add this kotlin block to set the JVM target compatibility
kotlin {
    jvmToolchain(17) // Match this to your Java version
}

// Your existing Java configuration
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    // Add toolchain configuration for consistency
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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

    // **Robolectric for Unit Testing**
    testImplementation(libs.robolectric)
}