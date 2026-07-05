plugins {
    `config-publish`

    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"

dependencies {
    api(project(":fusion-mojang"))
    api(project(":fusion-kyori"))
}

tasks {
    shadowJar {
        listOf(
            "org.spongepowered",
            "org.jspecify",
            "ch.jalu"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }
}