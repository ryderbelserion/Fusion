plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.name}.hytale"

repositories {
    maven("https://maven.hytale.com/release")
}

dependencies {
    api(project(":fusion-core"))

    api(libs.hytale.adventure)
    compileOnly(libs.hytale)
}