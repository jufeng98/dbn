package com.dbn.cache

import com.dbn.common.util.Strings

data class CacheDbDataType(
    val name: String?,
    val length: Long,
    val precision: Int,
    val scale: Int = 0,
    val set: Boolean,
) {
    val qualifiedName by lazy {
        initQualifiedName()
    }

    private fun initQualifiedName(): String {
        val sb = StringBuilder()
        val name: String? = name
        sb.append(if (name == null) "" else Strings.toLowerCase(name))
        if (precision > 0) {
            sb.append("(")
            sb.append(precision)
            if (scale > 0) {
                sb.append(", ")
                sb.append(scale)
            }
            sb.append(')')
        } else if (length > 0L) {
            sb.append(" (")
            sb.append(length)
            sb.append(')')
        }

        return sb.toString()
    }
}
