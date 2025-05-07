plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    id("paper-plugin")
}

base {
    archivesName.set("fusion-annotations-example")
}

project.group = "${rootProject.group}.paper"
project.version = "1.0.0"
project.description = "An example usage of Fusion Annotations for Paper based servers!"

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