package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin.ModelClassType
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.Interface
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.TopLevelClass
import org.mybatis.generator.api.dom.xml.Document
import org.mybatis.generator.api.dom.xml.TextElement

/**
 * @author yudong
 */
class CommentPlugin : PluginAdapter() {

    override fun validate(warnings: List<String>): Boolean {
        return true
    }

    override fun modelBaseRecordClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        val docLine = """
                /**
                 * ${introspectedTable.remarks},请勿手工改动此文件,请使用 mybatis generator
                 *
                 * @author mybatis generator
                 */
                 """.trimIndent()
        topLevelClass.addJavaDocLine(docLine)

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)
    }

    override fun modelFieldGenerated(
        field: Field, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: ModelClassType,
    ): Boolean {
        // 添加entity字段注释
        val docLine =
            """
    /**
     * ${introspectedColumn.remarks}
     */""".trimStart()
        field.addJavaDocLine(docLine)

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)
    }

    override fun modelGetterMethodGenerated(
        method: Method, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: ModelClassType,
    ): Boolean {
        val docLine = """
    /**
     * 获取${introspectedColumn.remarks}
     */""".trimStart()
        method.addJavaDocLine(docLine)

        return super.modelGetterMethodGenerated(
            method,
            topLevelClass,
            introspectedColumn,
            introspectedTable,
            modelClassType
        )
    }

    override fun modelSetterMethodGenerated(
        method: Method, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: ModelClassType,
    ): Boolean {
        // 添加entity set方法注释
        val docLine = """
    /**
     * 设置${introspectedColumn.remarks}
     */""".trimStart()
        method.addJavaDocLine(docLine)

        return super.modelGetterMethodGenerated(
            method,
            topLevelClass,
            introspectedColumn,
            introspectedTable,
            modelClassType
        )
    }

    override fun modelExampleClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        // 添加example类注释
        val docLine = """
                    /**
                     * 请勿手工改动此文件,请使用 mybatis generator
                     *
                     * @author mybatis generator
                     */
                     """.trimIndent()
        topLevelClass.addJavaDocLine(docLine)

        return super.modelExampleClassGenerated(topLevelClass, introspectedTable)
    }

    override fun clientGenerated(
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        // 添加mapper类注释
        val docLine = """
                    /**
                     * 操纵${introspectedTable.remarks},请勿手工改动此文件,请使用 mybatis generator
                     *
                     * @author mybatis generator
                     */
                     """.trimIndent()
        interfaze.addJavaDocLine(docLine)

        return super.clientGenerated(interfaze, introspectedTable)
    }


    override fun sqlMapDocumentGenerated(document: Document, introspectedTable: IntrospectedTable): Boolean {
        appendComment(document)

        return super.sqlMapDocumentGenerated(document, introspectedTable)
    }

    private fun appendComment(document: Document) {
        document.rootElement.addElement(
            0,
            TextElement("<!-- 此文件由 mybatis generator 生成,注意: 请勿手工改动此文件, 请使用 mybatis generator -->")
        )
    }


}
