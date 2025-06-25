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
    implementation(libs.configurate.json) {
        exclude("com.google.code.gson", "gson") // gson is present literally everywhere
    }

    annotationProcessor(libs.velocity)
    compileOnly(libs.velocity)

    api(project(":common"))
}

tasks {
    runVelocity {
        velocityVersion("3.4.0-SNAPSHOT")
    }
}