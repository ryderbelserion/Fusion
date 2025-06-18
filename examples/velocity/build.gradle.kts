plugins {
    id("xyz.jpenilla.run-velocity")

    `config-java`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":fusion-velocity"))

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks {
    runVelocity {
        velocityVersion("3.4.0-SNAPSHOT")
    }
}