plugins {
    id("com.gradleup.shadow")

    `java-library`
}

project.version = rootProject.version

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven("https://repo.crazycrew.us/releases")

    mavenCentral()
}

dependencies {
    compileOnly(libs.findLibrary("annotations").get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}