package com.dbn.mybatis.plugin

import com.dbn.mybatis.support.SelectOneElementGenerator
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.Interface
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.Parameter
import org.mybatis.generator.api.dom.xml.Document
import org.mybatis.generator.config.Context

/**
 * @author yudong
 */
class SelectOnePlugin : PluginAdapter() {
    var contextCp: Context? = null

    override fun setContext(context: Context) {
        this.contextCp = context
        super.setContext(context)
    }

    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    override fun clientGenerated(interfaze: Interface, introspectedTable: IntrospectedTable): Boolean {
        addSelectOneMethod(interfaze, introspectedTable)

        return super.clientGenerated(interfaze, introspectedTable)
    }

    override fun sqlMapDocumentGenerated(document: Document, introspectedTable: IntrospectedTable): Boolean {
        addSelectOneElement(document, introspectedTable)

        return super.sqlMapDocumentGenerated(document, introspectedTable)
    }

    private fun addSelectOneElement(document: Document, introspectedTable: IntrospectedTable) {
        val generator = SelectOneElementGenerator(this, introspectedTable)
        generator.addElements(document.rootElement)
    }

    private fun addSelectOneMethod(interfaze: Interface, introspectedTable: IntrospectedTable) {
        val method = Method("selectOneByExample")
        val type = introspectedTable.rules.calculateAllFieldsClass()
        method.isAbstract = true
        method.setReturnType(type)
        val exampleType = FullyQualifiedJavaType(introspectedTable.exampleType)
        method.addParameter(Parameter(exampleType, "example"))
        interfaze.addMethod(method)
    }
}