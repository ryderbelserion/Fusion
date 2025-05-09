rootProject.name = "fusion"

listOf(
    "core" to "core",
    "paper" to "paper",
    "fabric" to "fabric",
    "discord" to "discord",

    "examples/annotations" to "example-annotations",
    "examples/paper" to "example-paper",
    "examples/fabric" to "example-fabric",
    "examples/discord" to "example-discord"
).forEach(::includeProject)

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