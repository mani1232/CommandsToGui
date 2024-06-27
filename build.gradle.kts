plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "cc.worldmandia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.3")
    implementation("com.charleskorn.kaml:kaml-jvm:0.60.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.7.1")
    implementation("net.axay:kspigot:1.20.3")
    implementation("dev.jorel:commandapi-bukkit-shade:9.5.1")
    implementation("dev.jorel:commandapi-bukkit-kotlin:9.5.1")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    shadowJar {
        relocate("dev.jorel.commandapi", "$group.commandapi")
        relocate("net.axay.kspigot", "$group.kspigot")
    }
}