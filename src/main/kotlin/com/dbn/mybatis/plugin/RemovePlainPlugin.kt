package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter

/**
 * @author yudong
 */
class RemovePlainPlugin : PluginAdapter() {

    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    override fun initialized(introspectedTable: IntrospectedTable) {
        val allColumns = introspectedTable.baseColumns
        val iterator = allColumns.iterator()
        while (iterator.hasNext()) {
            val column = iterator.next()

            if (!column.actualColumnName.endsWith("_plain")) {
                continue
            }

            iterator.remove()
        }
    }

}