package com.dbn.mybatis.plugin

import com.dbn.utils.StringUtil
import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin.ModelClassType
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.JavaVisibility
import org.mybatis.generator.api.dom.java.TopLevelClass
import org.mybatis.generator.config.Context

class StaticFieldNamePlugin : PluginAdapter() {

    override fun setContext(context: Context) {
        super.setContext(context)
    }

    override fun validate(warnings: List<String>): Boolean {
        return true
    }

    override fun modelFieldGenerated(
        field: Field, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: ModelClassType,
    ): Boolean {
        val constStaticField = createConstField(field)

        topLevelClass.addField(constStaticField)

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)
    }

    private fun createConstField(field: Field): Field {
        val stringInstance = FullyQualifiedJavaType.getStringInstance()
        val constField = Field(StringUtil.wordsToConstantCase(field.name), stringInstance)
        constField.setInitializationString("\"" + field.name + "\"")
        constField.isTransient = false
        constField.isVolatile = false
        constField.visibility = JavaVisibility.PUBLIC
        constField.isFinal = true
        constField.isStatic = true
        return constField
    }
}
