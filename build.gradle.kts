import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.3.40"
    `java-gradle-plugin`
}

apply("$rootDir/gradle/functional-test.gradle")

group = "org.jetbrains.kotlin.g2kts"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(gradleKotlinDsl())

    testCompile("org.junit.jupiter:junit-jupiter:5.5.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}