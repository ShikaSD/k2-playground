# Repro for KT-60942

To repro, run ./gradlew :app:assembleDebug.

Uncommenting dependency in app module build.gradle.kts allows the type to be resolved correctly.
