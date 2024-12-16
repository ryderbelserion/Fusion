plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    id("fusion.base")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(projects.fusionPaper)

    compileOnly(libs.paper)
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
    }
}