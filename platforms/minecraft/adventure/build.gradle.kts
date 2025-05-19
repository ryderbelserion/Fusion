plugins {
    `config-java`
}

dependencies {
    api(project(":core"))

    compileOnly(libs.bundles.adventure)
}