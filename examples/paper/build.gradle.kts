plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    id("fusion.base")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    implementation(projects.fusionPaper)
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }
}