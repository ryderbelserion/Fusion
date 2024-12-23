plugins {
    id("fusion.base")
}

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.configurate.yaml)

    api(libs.configurate.jackson)
}