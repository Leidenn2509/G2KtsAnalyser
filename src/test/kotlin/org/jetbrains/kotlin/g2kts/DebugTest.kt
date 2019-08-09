package org.jetbrains.kotlin.g2kts

import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.junit.jupiter.api.Test

class DebugTest {
    private val projectDir = TemporaryFolder().apply {
        create()
    }


    @Test
    fun debugTest() {
        val buildFile = projectDir.newFile("build.gradle")
        buildFile.appendText("""
apply plugin: "org.jetbrains.kotlin.g2kts.g2ktsAnalyser"
        """.trimIndent())

//        val project = ProjectBuilder.builder().build()
//        GradleRunner.create()
//            .withProjectDir(projectDir.root)
//            .withPluginClasspath()
//            .build()
//        project.pluginManager.apply("scala")
//        project.pluginManager.apply("org.jetbrains.kotlin.g2kts.g2ktsAnalyser")

    }
}