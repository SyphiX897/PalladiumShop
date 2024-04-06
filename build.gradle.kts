plugins {
    id("java")
    id("maven-publish")
    id("io.papermc.paperweight.userdev") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

group = "ir.syphix"
version = "1.0.0"
description = "Simple and modern gui shop for Minecraft"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io/")
//    maven("https://repo.sayandevelopment.org/snapshots")
//    // Takenaka
//    maven("https://repo.screamingsandals.org/public")

}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    implementation("org.reflections:reflections:0.10.2")
    implementation ("com.github.Syrent:Origin:1.5.15")
//    implementation("org.sayandevelopment:stickynote-bukkit:1.0.9")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("com.github.Slimefun:Slimefun4:RC-37")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.1")

    compileOnly(fileTree("/libs"))
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.20.4")
    }

    processResources {
        filesMatching(listOf("**plugin.yml", "**plugin.json")) {
            expand(
                "version" to project.version as String,
                "name" to rootProject.name,
                "description" to project.description
            )
        }
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${version}.jar")
        archiveClassifier.set("")
        destinationDirectory.set(file(rootProject.projectDir.path + "/bin"))
        exclude("META-INF/**")
        from("LICENSE")
        minimize()
    }

    java {
        toolchain{
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        withJavadocJar()
        withSourcesJar()
    }

    jar {
        enabled = false
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenBuild") {
            from(components["java"])
            pom {
                name.set("palladiumshop")
                description.set(rootProject.description)
                url.set("https://github.com/SyphiX897/PalladiumShop")
                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://github.com/SyphiX897/PalladiumShop/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("syphix")
                        name.set("nima")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/syphix897/palladiumshop.git")
                    developerConnection.set("scm:git:ssh://github.com/syphix897/palladiumshop.git")
                    url.set("https://github.com/SyphiX897/PalladiumShop/tree/master")
                }
            }
        }
    }

    repositories {
        maven {
            name = "sayandevelopment-repo"
            url = uri("https://repo.sayandevelopment.org/snapshots/")

            credentials {
                username = project.findProperty("repo.sayan.user") as String
                password = project.findProperty("repo.sayan.token") as String
            }
        }
    }
}

