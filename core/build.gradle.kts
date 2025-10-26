plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.core"

dependencies {
    implementation(project(":fusion-files"))

    compileOnly(libs.bundles.adventure)
    compileOnly(libs.configurate.gson)
    compileOnly(libs.configurate.yaml)
    compileOnly(libs.jalu)
}