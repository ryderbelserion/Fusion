pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")

        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "fusion"

fun includeProject(pair: Pair<String, String>): Unit = includeProject(pair.first, pair.second)

fun includeProject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}

fun includeProject(path: String, name: String) {
    includeProject(name) {
        this.name = "${rootProject.name}-$name"
        this.projectDir = File(path)
    }
}

fun includeProject(name: String) {
    includeProject(name) {
        this.name = "${rootProject.name}-$name"
    }
}

listOf(
    "platforms/minecraft/neoforge" to "neoforge",
    "platforms/minecraft/fabric" to "fabric",
    "platforms/minecraft/paper" to "paper",

    "core" to "core"
).forEach(::includeProject)