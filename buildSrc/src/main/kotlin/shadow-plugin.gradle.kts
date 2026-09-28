plugins {
    id("com.gradleup.shadow")
    id("java-plugin")
}

tasks {
    shadowJar {
        mergeServiceFiles()

        archiveClassifier.set("")

        exclude("META-INF/**")
    }
}