import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9

plugins {
    alias(libs.plugins.multiplatform)
    id("com.android.library")
}

kotlin {
    androidTarget()

    targets.all {
        compilations.all {
            compilerOptions.configure {
                languageVersion.set(KOTLIN_1_9)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(libs.kotlinx.serialization.json)
            }
        }

        val jvmMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(jvmMain)
        }
    }
}

android {
    namespace = "com.example.kotlindevtest"
    compileSdk = 33

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}