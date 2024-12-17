package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.JavaVisibility
import org.mybatis.generator.api.dom.java.TopLevelClass

/**
 * @author yudong
 */
class SerializablePlugin : PluginAdapter() {

    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    override fun modelBaseRecordClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        topLevelClass.addImportedType("java.io.Serializable")

        topLevelClass.addSuperInterface(FullyQualifiedJavaType("Serializable"))

        addSerialVersionUID(topLevelClass)

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)
    }

    private fun addSerialVersionUID(topLevelClass: TopLevelClass) {
        val serialField = Field("serialVersionUID", FullyQualifiedJavaType("long"))
        serialField.visibility = JavaVisibility.PRIVATE
        serialField.isStatic = true
        serialField.isFinal = true
        serialField.setInitializationString("-1L")

        topLevelClass.fields.add(0, serialField)
    }
}