package com.dbn.mybatis.plugin

import com.dbn.mybatis.support.BatchInsertElementGenerator
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.Interface
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.Parameter
import org.mybatis.generator.api.dom.xml.Document
import org.mybatis.generator.config.Context

class BatchInsertPlugin : PluginAdapter() {
    var contextCp: Context? = null

    override fun setContext(context: Context) {
        this.contextCp = context
        super.setContext(context)
    }

    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    override fun clientGenerated(interfaze: Interface, introspectedTable: IntrospectedTable): Boolean {
        batchInsertMethod(interfaze, introspectedTable)

        return super.clientGenerated(interfaze, introspectedTable)
    }

    override fun sqlMapDocumentGenerated(document: Document, introspectedTable: IntrospectedTable): Boolean {
        addBatchInsertElement(document, introspectedTable)

        return super.sqlMapDocumentGenerated(document, introspectedTable)
    }

    private fun addBatchInsertElement(document: Document, introspectedTable: IntrospectedTable) {
        val generator = BatchInsertElementGenerator(this, introspectedTable)
        generator.addElements(document.rootElement)
    }

    private fun batchInsertMethod(interfaze: Interface, introspectedTable: IntrospectedTable) {
        val method = Method("batchInsert")
        val type = FullyQualifiedJavaType.getIntInstance()
        method.isAbstract = true
        method.setReturnType(type)
        val returnType = FullyQualifiedJavaType.getNewListInstance()
        val listType = if (introspectedTable.rules.generateRecordWithBLOBsClass()) {
            FullyQualifiedJavaType(introspectedTable.recordWithBLOBsType)
        } else {
            // the blob fields must be rolled up into the base class
            FullyQualifiedJavaType(introspectedTable.baseRecordType)
        }
        returnType.addTypeArgument(listType)
        method.addParameter(Parameter(returnType, "list"))
        interfaze.addMethod(method)
    }
}