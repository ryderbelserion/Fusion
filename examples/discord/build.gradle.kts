plugins {
    alias(libs.plugins.shadow)

    id("root-plugin")

    application
}

project.group = "${rootProject.group}.discord"
project.version = "1.0.0"
project.description = "An example usage of Fusion for Discord Bots!"

repositories {

}

dependencies {
    api(project(":fusion-discord"))
}

application {
    mainClass.set("com.ryderbelserion.fusion.discord.Starter")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
}