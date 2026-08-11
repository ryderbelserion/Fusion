pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "Fusion"

listOf(
    "minecraft/paper/example" to "example",

    "minecraft/velocity" to "velocity",
    "minecraft/paper" to "paper",
    "minecraft/kyori" to "kyori",

    "standalone/core" to "core",

    "hytale" to "hytale",
    "api" to "api"
).forEach {
    includeProject(it.first, it.second)
}

fun includeProject(name: String) {
    includeProject(name) {
        this.name = "${rootProject.name.lowercase()}-$name"
    }
}

fun includeProject(folder: String, name: String) {
    includeProject(name) {
        this.name = "${rootProject.name.lowercase()}-$name"
        this.projectDir = file(folder)
    }
}

fun includeProject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}