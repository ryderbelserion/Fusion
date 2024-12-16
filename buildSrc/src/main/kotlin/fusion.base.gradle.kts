plugins {
    id("fusion.parent")
}

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    withSourcesJar()
    withJavadocJar()
}

tasks {
    compileJava {
        options.encoding = "UTf-8"
        options.compilerArgs.add("-parameters")
        options.release.set(21)
    }

    javadoc {
        options.encoding = "UTf-8"
    }

    processResources {
        filteringCharset = "UTf-8"
    }
}