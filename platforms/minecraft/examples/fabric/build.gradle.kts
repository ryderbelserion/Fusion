plugins {
    `config-java`
}

dependencies {
    api(project(":fusion-fabric"))
}

tasks.register("runFabric") {
    this.group = "run fabric"
}