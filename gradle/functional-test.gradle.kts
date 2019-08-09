import org.gradle.api.internal.HasConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    kotlin("jvm") version "1.3.40"
}

val SourceSet.kotlin: SourceDirectorySet
    get() = (this as HasConvention).convention.getPlugin<KotlinSourceSet>().kotlin
var SourceDirectorySet.sourceDirs: Iterable<File>
    get() = srcDirs
    set(value) { setSrcDirs(value) }

sourceSets {
    create("functionalTest") {
        kotlin.sourceDirs = files("src/functionalTest/kotlin")
        resources.sourceDirs = files("src/functionalTest/resources")
        compileClasspath += sourceSets["main"].output + configurations.testRuntime
        runtimeClasspath += output + compileClasspath
    }
}

tasks.create<Test>("functionalTest") {
    description = "Runs the functional tests."
    group = "verification"
    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
    mustRunAfter(tasks["test"])
}

tasks["check"].dependsOn(tasks["functionalTest"])

// tag::source-set-config[]
gradlePlugin {
    testSourceSets(sourceSets["functionalTest"])
}
// end::source-set-config[]