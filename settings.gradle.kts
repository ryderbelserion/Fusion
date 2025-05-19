rootProject.name = "fusion"

//listOf(
    //"core" to "core",
    //"paper" to "paper",
    //"fabric" to "fabric",
    //"discord" to "discord",

    //"examples/discord" to "example-discord",
    //"examples/fabric" to "example-fabric",
    //"examples/paper" to "example-paper",
//).forEach(::includeProject)

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
    "platforms/adventure" to "adventure",
    "platforms/paper" to "paper"
).forEach(::includeProject)

include("paper","core")