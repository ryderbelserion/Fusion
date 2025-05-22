plugins {
    `config-java`
}

project.group = "${rootProject.name}.core"

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.configurate.yaml)

    api(libs.configurate.json)

    api(libs.jalu)
}