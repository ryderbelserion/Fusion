plugins {
    `config-publish`

    `velocity-plugin`
}

project.group = "${rootProject.group}.velocity"

dependencies {
    api(project(":fusion-kyori"))

    implementation(libs.configurate.gson)
    implementation(libs.configurate.yaml)
    implementation(libs.jspecify)
}