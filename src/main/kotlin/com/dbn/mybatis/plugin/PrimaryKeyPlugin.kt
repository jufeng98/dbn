package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.xml.Attribute
import org.mybatis.generator.api.dom.xml.XmlElement

class PrimaryKeyPlugin : PluginAdapter() {

    override fun validate(warnings: MutableList<String>?): Boolean {
        return true
    }

    override fun sqlMapInsertElementGenerated(element: XmlElement, introspectedTable: IntrospectedTable): Boolean {
        return addGenerateKey(element, introspectedTable)
    }

    override fun sqlMapInsertSelectiveElementGenerated(
        element: XmlElement,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return addGenerateKey(element, introspectedTable)
    }

    private fun addGenerateKey(element: XmlElement, introspectedTable: IntrospectedTable): Boolean {
        val columns = introspectedTable.primaryKeyColumns
        if (columns.size == 1 && columns[0].isAutoIncrement) {
            element.addAttribute(Attribute("useGeneratedKeys", "true"))
            element.addAttribute(Attribute("keyProperty", columns[0].javaProperty))
        }

        return super.sqlMapInsertElementGenerated(element, introspectedTable)
    }
}