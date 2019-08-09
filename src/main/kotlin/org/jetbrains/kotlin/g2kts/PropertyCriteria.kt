package org.jetbrains.kotlin.g2kts

import kotlin.reflect.KType

data class PropertyCriteria(
    var haveSet: Boolean = false,
    var setType: KType? = null,
    var haveGet: Boolean = false,
    var getType: KType? = null
) {

}