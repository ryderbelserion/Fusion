plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)

    id("fusion.base")
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

    api(projects.fusionCore)
}