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

    implementation(libs.configurate.gson)
    implementation(libs.configurate.yaml)
    implementation(libs.jspecify)

    api(libs.hytale.adventure)
    compileOnly(libs.hytale)
}