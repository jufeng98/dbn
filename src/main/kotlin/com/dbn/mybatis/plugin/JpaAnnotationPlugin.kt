package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.TopLevelClass
import org.mybatis.generator.internal.util.StringUtility

/**
 * @author yudong
 */
class JpaAnnotationPlugin : PluginAdapter() {

    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    override fun modelBaseRecordClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        topLevelClass.addImportedType("javax.persistence.*")

        var tableName = introspectedTable.fullyQualifiedTableNameAtRuntime
        if (StringUtility.stringContainsSpace(tableName)) {
            tableName = context.beginningDelimiter + tableName + context.endingDelimiter
        }

        topLevelClass.addAnnotation("@Table(name = \"$tableName\")")

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)
    }

    override fun modelFieldGenerated(
        field: Field,
        topLevelClass: TopLevelClass,
        introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable,
        modelClassType: Plugin.ModelClassType,
    ): Boolean {
        for (column in introspectedTable.primaryKeyColumns) {
            if (introspectedColumn === column) {
                field.addAnnotation("@Id")
                break
            }
        }

        val beginningDelimiter = introspectedColumn.context.beginningDelimiter
        val endingDelimiter = introspectedColumn.context.endingDelimiter

        var column = introspectedColumn.actualColumnName
        if (StringUtility.stringContainsSpace(column) || introspectedTable.tableConfiguration.isAllColumnDelimitingEnabled) {
            column = beginningDelimiter + column + endingDelimiter
        }

        field.addAnnotation("@Column(name = \"$column\")")

        annotatorGeneratedKeyColumn(field, introspectedColumn, introspectedTable)

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)
    }

    private fun annotatorGeneratedKeyColumn(
        field: Field,
        introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable,
    ) {
        val generatedKeyOptional = introspectedTable.tableConfiguration.generatedKey
        if (!generatedKeyOptional.isPresent) {
            return
        }

        val generatedKey = generatedKeyOptional.get()
        if (generatedKey.column != introspectedColumn.actualColumnName) {
            return
        }

        val statement = generatedKey.runtimeSqlStatement
        if (introspectedColumn.isIdentity) {
            if (statement == "JDBC") {
                field.addAnnotation("@GeneratedValue(generator = \"$statement\")")
            } else {
                field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)")
            }
            return
        }

        if (introspectedColumn.isSequenceColumn) {
            field.addAnnotation("@SequenceGenerator(name = \"\", sequenceName = \"$statement\")")
        }
    }
}