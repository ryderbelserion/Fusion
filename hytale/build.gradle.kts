plugins {
    `config-publish`
    `shadow-plugin`
}

project.group = "${rootProject.name}.hytale"

repositories {
    maven("https://maven.hytale.com/release")
}

dependencies {
    api(project(":fusion-kyori"))

    implementation(libs.configurate.gson)
    implementation(libs.configurate.yaml)
    implementation(libs.jspecify)

    api(libs.bundles.adventure) {
        exclude(group = "net.kyori", module = "adventure-text-serializer-legacy")
        exclude(group = "net.kyori", module = "adventure-text-logger-slf4j")
    }

    compileOnly(libs.hytale)
}

/*tasks.register<Exec>("runHytaleServer") {
    description = "Runs the hytale server!"
    group = "Hytale"

    dependsOn("extractHytaleServer")
    workingDir("$projectDir/run")

    //commandLine("hytale-downloader")
}

tasks.register<Exec>("extractHytaleServer") {
    description = "Extracts the hytale server and downloads it!"
    group = "Hytale"

    dependsOn("fetchHytaleServer")
    workingDir("$projectDir/run/cache")

    commandLine("tar", "-xvzf", "hytale-downloader.zip")
}

tasks.register<Exec>("fetchHytaleServer") {
    description = "Fetch the latest hytale server!"
    group = "Hytale"

    dependsOn("createHytaleFolder")
    workingDir("$projectDir/run")

    executable("curl")
    args("--output-dir", "cache", "-O", "https://downloader.hytale.com/hytale-downloader.zip")
}

tasks.register("createHytaleFolder") {
    description = "Creates the hytale directory!"
    group = "Hytale"

    doFirst {
        val path = projectDir.toPath().resolve("run")

        if (!Files.exists(path)) {
            Files.createDirectory(path)
        }

        val cache = path.resolve("cache")

        if (!Files.exists(cache)) {
            Files.createDirectory(cache)
        }
    }
}*/