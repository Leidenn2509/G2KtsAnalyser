package org.jetbrains.kotlin.g2kts

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.task

open class G2KtsStatCollectorExtension {
}

class G2KtsStatCollector : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<G2KtsStatCollectorExtension>("g2kts")
        project.task<Task>("g2ktsCollector") {
            doLast {
                println("collect...")
            }
        }

    }
}