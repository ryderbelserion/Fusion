plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.api"

dependencies {
    compileOnly(libs.bundles.adventure)
}