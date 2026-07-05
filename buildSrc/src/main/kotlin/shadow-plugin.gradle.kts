plugins {
    id("com.gradleup.shadow")
    id("java-plugin")
}

tasks {
    shadowJar {
        archiveClassifier.set("")

        exclude("META-INF/**")

        listOf(
            "io.leangen.geantyref",
            "org.spongepowered",
            "com.google.gson",
            "org.jspecify",
            "org.yaml",
            "ch.jalu"
        ).forEach {
            relocate(it, "libs.$it")
        }
    }
}