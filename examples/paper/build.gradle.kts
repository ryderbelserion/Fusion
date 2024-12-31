plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    id("fusion.base")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repo.triumphteam.dev/snapshots/")

    maven("https://repo.nexomc.com/snapshots/")

    maven("https://repo.oraxen.com/releases/")
}

dependencies {
    implementation(projects.fusionPaper)

    implementation(libs.triumph.cmds)

    compileOnly(libs.bundles.shared) {
        exclude("org.bukkit", "*")
    }

    compileOnly(libs.paper)
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }
}