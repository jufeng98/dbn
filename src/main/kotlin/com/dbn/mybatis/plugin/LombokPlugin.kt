package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.TopLevelClass


class LombokPlugin : PluginAdapter() {

    override fun validate(warnings: List<String?>?): Boolean {
        return true
    }

    override fun modelBaseRecordClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        addLombok(topLevelClass)

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)
    }

    override fun modelGetterMethodGenerated(
        method: Method,
        topLevelClass: TopLevelClass,
        introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable,
        modelClassType: Plugin.ModelClassType,
    ): Boolean {
        return false
    }

    override fun modelSetterMethodGenerated(
        method: Method,
        topLevelClass: TopLevelClass,
        introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable,
        modelClassType: Plugin.ModelClassType,
    ): Boolean {
        return false
    }

    private fun addLombok(topLevelClass: TopLevelClass) {
        topLevelClass.addImportedType("lombok.AllArgsConstructor")
        topLevelClass.addImportedType("lombok.Builder")
        topLevelClass.addImportedType("lombok.Data")
        topLevelClass.addImportedType("lombok.NoArgsConstructor")

        topLevelClass.addAnnotation("@Data")
        topLevelClass.addAnnotation("@Builder")
        topLevelClass.addAnnotation("@AllArgsConstructor")
        topLevelClass.addAnnotation("@NoArgsConstructor")
    }

}
