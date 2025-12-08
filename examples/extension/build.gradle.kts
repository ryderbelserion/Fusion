plugins {
    `config-java`
}

dependencies {
    compileOnly(project(":fusion-addons"))
    compileOnly(libs.log4j)
}