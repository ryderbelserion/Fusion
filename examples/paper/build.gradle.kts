plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    id("paper-plugin")
}

base {
    archivesName.set("fusion-paper-example")
}

project.group = "${rootProject.group}.paper"
project.version = "1.0.0"
project.description = "An example usage of Fusion for Paper based servers!"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repo.triumphteam.dev/snapshots/")

    maven("https://repo.nexomc.com/snapshots/")

    maven("https://repo.oraxen.com/releases/")

    maven("https://maven.devs.beer/")
}

dependencies {
    implementation(libs.triumph.cmds)

    compileOnly(libs.bundles.shared) {
        exclude("org.bukkit", "*")
    }

    api(project(":fusion-paper"))
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }

    shadowJar {
        archiveClassifier.set("")
    }
}