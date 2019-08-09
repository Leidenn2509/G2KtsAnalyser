package org.jetbrains.kotlin.g2kts

import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import java.io.File

class FunctionalTest {
    private val projectDir = TemporaryFolder().apply {
        create()
    }

    @Test
    fun functionalTest() {
        val buildFile = projectDir.newFile("build.gradle")

        File("src/functionalTest/resources/gradle").copyTo(buildFile, true)
//        buildFile.appendText("""
//plugins {
//    id "org.jetbrains.kotlin.g2kts.G2KtsAnalyser"
//}
//        """.trimIndent())
        val res = GradleRunner.create()
            .withProjectDir(projectDir.root)
            .withPluginClasspath()
            .withArguments("g2kts")
            .build()
        println(res.output)
    }
}