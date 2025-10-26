plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.files"

dependencies {
    implementation(libs.configurate.gson)
    implementation(libs.configurate.yaml)
    implementation(libs.jalu)
}