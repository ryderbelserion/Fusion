plugins {
    `config-publish`
    `java-plugin`
}

project.group = "${rootProject.name}.hytale"

repositories {
    maven("https://maven.hytale.com/release")
}

dependencies {
    api(project(":fusion-kyori"))

    api(libs.bundles.adventure) {
        exclude(group = "net.kyori", module = "adventure-text-serializer-legacy")
        exclude(group = "net.kyori", module = "adventure-text-logger-slf4j")
    }

    compileOnly(libs.hytale)
}