plugins {
    `config-paper`
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.triumphteam.dev/snapshots")

    maven("https://repo.nexomc.com/snapshots")

    maven("https://repo.oraxen.com/releases")
}

dependencies {
    implementation(libs.triumph.cmds)

    api(project(":fusion-paper"))
}

tasks {
    runPaper.folia.registerTask()

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }
}