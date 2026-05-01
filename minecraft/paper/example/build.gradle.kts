plugins {
    `config-publish`
    `config-paper`
}

project.group = "${rootProject.name}.paper"

repositories {

}

dependencies {
    implementation(project(":fusion-commands"))
    //implementation(project(":fusion-paper"))
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