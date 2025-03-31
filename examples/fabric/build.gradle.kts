plugins {
    alias(libs.plugins.shadow)

    id("root-plugin")
}

project.group = "${rootProject.group}.fabric"
project.version = rootProject.version
project.description = "An example usage of Fusion for Fabric based servers!"

repositories {

}

dependencies {
    implementation(project(":fusion-fabric"))
}