plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.maps.secrets) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.dependencyAnalysis)
    alias(libs.plugins.gradleDoctor)
    alias(libs.plugins.kotlin.compose) apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory}/**/*.kt")

            ktlint()
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
        }

        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/**/*.xml")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}

tasks.register("updateGitHooks", Copy::class) {
    from("./scripts/pre-commit")
    into("./.git/hooks")
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
    dependsOn("updateGitHooks")
}
