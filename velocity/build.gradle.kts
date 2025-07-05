plugins {
    id("xyz.jpenilla.run-velocity")

    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.velocity"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    annotationProcessor(libs.velocity)
    compileOnly(libs.velocity)

    api(project(":core"))
}

tasks {
    runVelocity {
        velocityVersion("3.4.0-SNAPSHOT")
    }
}