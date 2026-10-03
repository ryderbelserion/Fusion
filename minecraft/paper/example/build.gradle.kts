plugins {
    `paper-plugin`
}

project.group = "${rootProject.name}.paper"

dependencies {
    api(project(":fusion-addons"))
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
}