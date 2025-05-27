plugins {
    `config-publish`
    `config-java`
}

dependencies {
    api(project(":fusion-core"))

    compileOnly(libs.bundles.adventure)
}