plugins {
    `config-publish`
    `config-paper`
}

project.group = "${rootProject.name}.paper"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repo.nexomc.com/releases/")

    maven("https://repo.oraxen.com/releases/")

    maven("https://maven.devs.beer/")
}

dependencies {
    implementation(libs.jalu) {
        exclude(
            group = "org.yaml",
            module = "snakeyaml",
        )
    }

    compileOnly(libs.bundles.shared)

    api(project(":fusion-core"))
}

tasks {
    runPaper.folia.registerTask()

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }
}