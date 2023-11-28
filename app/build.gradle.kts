import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.com.android.application)
}


dependencies {
    kotlinCompilerPluginClasspath("androidx.compose.compiler:compiler:1.5.5-dev-k2.0.0-Beta1-06b8ae672a4")
}

kotlin {
    androidTarget()

    targets.all {
        compilations.all {
            compilerOptions.configure {
                languageVersion.set(KotlinVersion.KOTLIN_1_9)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(project(":module"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(jvmMain)
        }

        val androidUnitTest by getting {
            dependsOn(commonTest)

            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

android {
    namespace = "com.example.kotlindevtestapp"
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