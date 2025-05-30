plugins {
    id("com.gradleup.shadow")

    `java-library`
}

project.version = rootProject.version

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven("https://repo.codemc.io/repository/maven-public")

    maven("https://repo.crazycrew.us/libraries")
    maven("https://repo.crazycrew.us/releases")

    maven("https://jitpack.io")

    mavenCentral()
}

dependencies {
    compileOnly(libs.findLibrary("annotations").get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withSourcesJar()
    withJavadocJar()
}

tasks {
    shadowJar {
        exclude("META-INF/**")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}