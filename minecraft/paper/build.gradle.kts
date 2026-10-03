plugins {
    `config-publish`
    `paper-plugin`
}

project.group = "${rootProject.name}.paper"

dependencies {
    api(project(":fusion-kyori"))

    compileOnly(libs.bundles.shared)
}

tasks {
    shadowJar {
        relocate("org.spongepowered:configurate-yaml", "${project.group}.internal.yaml")
        relocate("org.spongepowered:configurate-gson", "${project.group}.internal.gson")
    }
}