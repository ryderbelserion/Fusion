plugins {
    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"

dependencies {
    api(project(":fusion-mojang"))
    api(project(":fusion-kyori"))
}