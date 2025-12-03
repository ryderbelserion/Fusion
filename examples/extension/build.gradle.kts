plugins {
    `config-java`
}

dependencies {
    compileOnly("ch.qos.logback:logback-classic:1.5.20")
    compileOnly(project(":fusion-addons"))
}