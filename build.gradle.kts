import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.40"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.1"
}

pluginBundle {
    website = "https://github.com/Leidenn2509"
    vcsUrl = "https://github.com/Leidenn2509"
    tags = listOf("g2kts", "groovy", "kotlin", "gradle")
}

group = "org.jetbrains.kotlin.g2kts"
version = "0.0.1"

gradlePlugin {
    plugins {
        create("g2ktsAnalyser") {
            id = "org.jetbrains.kotlin.g2kts.g2ktsAnalyser"
            displayName = "G2Kts Analyser"
            description = "Analyse build script for g2kts"
            implementationClass = "org.jetbrains.kotlin.g2kts.G2KtsAnalyser"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleKotlinDsl())
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}