plugins {
    `config-publish`
    `config-java`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(project(":fusion-core")) {
        exclude("com.google.errorprone", "error_prone_annotations")
        exclude("org.spongepowered", "configurate-core")
    }

    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks {
    shadowJar {
        exclude("fusion.yml")
    }

    jar {
        exclude("fusion.yml")
    }
}