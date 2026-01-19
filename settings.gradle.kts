pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Fusion"

listOf(
    //"minecraft/kyori" to "kyori",

    "minecraft/paper" to "paper",
    "minecraft/kyori" to "kyori",

    "standalone/files" to "files",
    "standalone/core" to "core"
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