plugins {
    `config-publish`

    `paper-plugin`
}

project.group = "${rootProject.name}.paper"

dependencies {
    api(project(":fusion-paper"))

    compileOnly(libs.bundles.shared)
}

tasks {
    runPaper.folia.registerTask()

    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")
        jvmArgs("-Dcom.mojang.eula.agree=true")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }

    shadowJar {
        listOf(
            "io.leangen.geantyref",
            "org.spongepowered",
            "com.google.gson",
            "org.jspecify",
            "org.yaml",
            "ch.jalu"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }
}