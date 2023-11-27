import org.gradle.configurationcache.extensions.capitalized

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.com.android.application)
}


dependencies {
//    kotlinCompilerPluginClasspath(project(":compiler-plugin"))
}

android {
    applicationVariants.configureEach {
        val variant = this
        val outputDir = File(buildDir, "generateExternalFile/${variant.dirName}")
        val task = project.tasks.register("generateExternalFile${variant.name.capitalized()}", FileGeneratingTask::class.java) {
            this.outputDir.set(outputDir)
        }
        variant.registerJavaGeneratingTask(task, outputDir)
    }
}

kotlin {
    androidTarget()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
//                implementation(project(":k1"))
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

@DisableCachingByDefault
abstract class FileGeneratingTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun taskAction() {
        println("Task!")
        val outputDirFile = outputDir.asFile.get()
        outputDirFile.mkdirs()
        val file = File(outputDirFile, "Test.kt")
        val text = """
            val hello = "World!"
        """
        file.writeText(text)
    }
}