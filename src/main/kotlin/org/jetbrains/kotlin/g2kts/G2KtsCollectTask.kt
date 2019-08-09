package org.jetbrains.kotlin.g2kts

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import kotlin.reflect.*
import kotlin.reflect.full.isSubtypeOf

open class G2KtsCollectTask : DefaultTask() {
    private fun <K, V> MutableMap<K, MutableList<V>>.putOrMergeAll(pairs: Iterable<Pair<K, V>>) {
        pairs.forEach { (k, v) ->
            if (containsKey(k)) {
                get(k)!!.add(v)
            } else {
                put(k, mutableListOf(v))
            }
        }
    }

    fun oldCollect() {
        val res = mutableMapOf<String, MutableList<MemberType>>()
        res.putAll(project.tasks.names.map { it to mutableListOf(MemberType.TASK) })
        res.putOrMergeAll(project.extensions.schema.keys.map { it to MemberType.EXTENSION })
        res.putOrMergeAll(project::class.getVars().map { it to MemberType.VAR })
        logger.quiet(res.toList().joinToString(separator = ",\n") { (k, v) ->
            "\"$k\" to ${v.joinToString(prefix = "listOf(", postfix = ")", separator = ", ") { "MemberType.$it" }}"
        })
    }

    fun List<String>.toStringLikeList(name: String) = joinToString(
        prefix = "val $name = listOf(\n\t",
        separator = ",\n\t",
        postfix = "\n)"
    ) { "\"$it\"" }

    @TaskAction
    fun collect() {
        val tasks = project.tasks.names.toList()
        val extensions = project.extensions.schema.keys.toList()
        val vars = project::class.getVars()
        logger.quiet(tasks.toStringLikeList("task"))
        logger.quiet(extensions.toStringLikeList("extensions"))
        logger.quiet(vars.toStringLikeList("vars"))
    }
//    data class ClassMember(
//        var name: String,
//        var type: ClassMemberType
//    ) {
//        enum class ClassMemberType {
//            SET, GET, FUN, VAR
//        }
//    }
//
//    fun List<ClassMember>.add(name: String, type: ClassMember.ClassMemberType) {
//        find { it.name == name }?.let {
//
//        }
//    }
//
//    private fun KClass<*>.membersWithType(): List<ClassMember> {
//        val res = mutableListOf<ClassMember>()
//        for (member in members) {
//            if (member.visibility != KVisibility.PUBLIC) continue
//
//            val r = when (member) {
//                is KProperty -> res.add()
//                is KFunction -> TODO()
//                else -> TODO()
//            }
//        }
//
//        TODO()
//    }

    private fun KClass<*>.getVars(): List<String> {
        val res = mutableListOf<String>()
        val buf = mutableMapOf<String, PropertyCriteria>()
        for (member in members) {
            if (member.visibility != KVisibility.PUBLIC) continue
            when (member) {
                is KMutableProperty, is KProperty -> res.add(member.name)
                is KFunction -> {
                    when {
                        member.name.startsWith("set") &&
                                member.parameters.size == 2 &&
                                member.typeParameters.isEmpty() &&
                                member.returnType.classifier == Unit::class -> {
                            val key = member.name.removePrefix("set").decapitalize()
                            if (buf.containsKey(key)) {
                                buf[key]!!.apply {
                                    haveSet = true
                                    setType = member.parameters.first().type
                                }
                            } else {
                                buf[key] =
                                    PropertyCriteria(haveSet = true, setType = member.parameters.first().type)
                            }
                        }
                        member.name.startsWith("get") &&
                                member.parameters.size == 1 &&
                                member.typeParameters.isEmpty() -> {
                            val key = member.name.removePrefix("get").decapitalize()
                            if (buf.contains(key)) {
                                buf[key]!!.apply {
                                    haveGet = true
                                    getType = member.returnType
                                }
                            } else {
                                buf[key] = PropertyCriteria(haveGet = true, getType = member.returnType)
                            }
                        }
                    }

                }
                else -> throw UnsupportedOperationException("Unknown callable: $this")
            }
        }
        res.addAll(buf.filterValues {
            it.haveGet && it.haveSet && (it.setType?.isSubtypeOf(it.getType ?: return@filterValues false) ?: false)
        }.keys)
        return res
    }
}