plugins {
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper")

    id("shadow-plugin")
}

val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

repositories {
    maven("https://papermc.io/repo/repository/maven-public")

    maven("https://repo.extendedclip.com/releases")

    maven("https://repo.momirealms.net/releases")

    maven("https://repo.hibiscusmc.com/releases")

    maven("https://repo.nexomc.com/releases")

    maven("https://repo.oraxen.com/releases")
}

dependencies {
    paperweight.paperDevBundle(libs.findVersion("paper").get().toString())
}