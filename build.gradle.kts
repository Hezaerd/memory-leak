plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom-remap") version "1.15-SNAPSHOT"
    `maven-publish`
    id("me.modmuss50.mod-publish-plugin") version "1.0.+"
}

version = "${project.property("mod_version")}+${stonecutter.current.version}"
group = project.property("maven_group")!!

base {
	archivesName.set(project.property("archives_base_name") as String)
}

repositories {
}

dependencies {
    "minecraft"("com.mojang:minecraft:${property("minecraft_version")}")
    "mappings"(loom.officialMojangMappings())
    "modImplementation"("net.fabricmc:fabric-loader:${property("loader_version")}")
    "modImplementation"("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
}

tasks.processResources {
    val properties = mapOf(
        "version" to project.version,
        "minecraft_version" to project.property("minecraft_version"),
        "minecraft_version_out" to project.property("minecraft_version_out"),
        "loader_version" to project.property("loader_version")
    )
    properties.forEach { inputs.property(it.key, it.value) }
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(properties)
    }
}

tasks.register<DefaultTask>("collectFile") {
    group = "build"
    mustRunAfter("build")

    doLast {
        copy {
            from(
                file(
                    "build/libs/${project.property("archives_base_name")}-${project.property("mod_version")}+${project.property("minecraft_version")}.jar"
                )
            )
            into(rootProject.file("build/libs"))
        }
    }
}

tasks.register<DefaultTask>("buildAndCollect") {
    group = "build"
    dependsOn(tasks.named("build"), tasks.named("collectFile"))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = 21
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.property("archives_base_name")}" }
    }
}

publishMods {
    file = tasks.named<org.gradle.jvm.tasks.Jar>("remapJar").flatMap { it.archiveFile }
    changelog = if (file("CHANGELOG.md").exists()) file("CHANGELOG.md").readText() else "No changelog provided."
    version = "${property("mod_version")}+mc${stonecutter.current.version}"
    displayName = "Memory Leak ${property("mod_version")} for ${stonecutter.current.version}"
    type = STABLE
    modLoaders.add("fabric")

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        projectId = "Aqk3l0pH"
        minecraftVersions.add(stonecutter.current.version)
        projectDescription = providers.fileContents(rootProject.layout.projectDirectory.file("README.md")).asText

        requires("fabric-api")
    }
}