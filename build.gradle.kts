plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "cc.worldmandia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://maven.pvphub.me/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    compileOnly("com.charleskorn.kaml:kaml-jvm:0.60.0")
    compileOnly("dev.triumphteam:triumph-gui:3.1.10")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.7.0")
    implementation("com.mattmx:ktgui:2.4.0")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    shadowJar {
        relocate("com.mattmx", "$group.ktgui")
    }
}