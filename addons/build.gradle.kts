plugins {
    `config-publish`
    `config-java`
}

project.group = "${rootProject.name}.addons"
project.version = "0.2.0"

dependencies {
    compileOnly("ch.qos.logback:logback-classic:1.5.20")
}