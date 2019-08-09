package org.jetbrains.kotlin.g2kts

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import kotlin.reflect.*
import kotlin.reflect.full.isSubtypeOf

open class G2KtsAnalyserExtension {
}


class G2KtsAnalyser : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<G2KtsAnalyserExtension>("g2kts")
        project.tasks.create<G2KtsCollectTask>("g2kts")
    }
}