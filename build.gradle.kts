plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.71"
}
repositories {
    mavenCentral()
}
kotlin {
    // For ARM, should be changed to iosArm32 or iosArm64
    // For Linux, should be changed to e.g. linuxX64
    // For MacOS, should be changed to e.g. macosX64
    // For Windows, should be changed to e.g. mingwX64
    mingwX64("mingw") {
        val main by compilations.getting
        // Libraries of the GL family
        val glfw by main.cinterops.creating
        val glew by main.cinterops.creating
        binaries {
            executable {
                // Change to specify fully qualified name of your application"s entry point:
               entryPoint = "sample.main"
                // Specify command-line arguments, if necessary:
                runTask?.args("")
            }
        }
    }
    sourceSets {
        // Note: To enable common source sets please comment out "kotlin.import.noCommonSourceSets" property
        // in gradle.properties file and re-import your project in IDE.
        commonMain {}
        commonTest {}
    }
}

// Use the following Gradle tasks to run your application:
// :runReleaseExecutableMingw - without debug symbols
// :runDebugExecutableMingw - with debug symbols