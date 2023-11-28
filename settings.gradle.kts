pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
        }
        maven {
            url = uri("https://androidx.dev/storage/compose-compiler/repository/")
        }
    }
}

rootProject.name = "Kotlin Dev test"
include(":app")
include(":module")
include(":compiler-plugin")
