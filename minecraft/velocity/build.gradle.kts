plugins {
    `config-publish`

    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"

dependencies {
    api(project(":fusion-kyori")) {
        exclude(group = "org.jspecify")
    }
}