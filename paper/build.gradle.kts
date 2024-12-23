plugins {
    id("fusion.base")

    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    maven("https://repo.nexomc.com/snapshots/")

    maven("https://repo.oraxen.com/releases/")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

    compileOnly(libs.bundles.shared) {
        exclude("org.bukkit", "*")
    }

    api(projects.fusionCore)
}