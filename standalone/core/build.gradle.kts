plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.name}.core"

dependencies {
    api(project(":fusion-api")) {
        exclude(group = "org.jspecify")
        exclude(group = "org.tinylog")
    }

    implementation(libs.tinylog.impl)
    implementation(libs.tinylog.api)
    implementation(libs.jspecify)

    api(libs.configurate.gson)
    api(libs.configurate.yaml)
}