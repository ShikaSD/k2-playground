plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.com.android.application)
}


dependencies {
//    kotlinCompilerPluginClasspath(project(":compiler-plugin"))
}

kotlin {
    androidTarget()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("test"))
//                implementation(project(":k1"))
            }
        }

        val commonTest by getting

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