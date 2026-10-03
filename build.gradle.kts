plugins {
    `java-plugin`
}

rootProject.group = "com.ryderbelserion.fusion"
rootProject.version = "4.52.0"

tasks.register("publishLocally") {
    description = "Publishes the library to the local repository!"
    group = "fusion"

    dependsOn(subprojects.filter { it.name != "fusion-weeb-addon" || it.name != "fusion-example" }.map { it.tasks.matching { it.name == "publishToMavenLocal" } })
}

tasks.register("publish") {
    description = "Publishes the library to the remote repository!"
    group = "fusion"

    dependsOn(subprojects.filter { it.name != "fusion-weeb-addon" || it.name != "fusion-example" }.map { it.tasks.matching { it.name == "publish" } })
}