plugins {
    id("xyz.jpenilla.run-velocity")

    id("shadow-plugin")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    annotationProcessor(libs.findLibrary("velocity").get())
    compileOnly(libs.findLibrary("velocity").get())
}