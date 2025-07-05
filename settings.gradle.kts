pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")

        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Fusion"

include("velocity", "neoforge", "core", "addons", "fabric", "paper")