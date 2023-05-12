import org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.com.android.application)
}

kotlin {
    androidTarget()

    targets.all {
        compilations.all {
            compilerOptions.configure {
                languageVersion.set(KOTLIN_2_0)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }

        val jvmMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(jvmMain)

            dependencies {
                implementation(libs.appcompat)
                implementation(libs.material)
            }
        }
    }
}

android {
    namespace = "com.example.kotlindevtest"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.kotlindevtest"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}