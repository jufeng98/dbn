package com.dbn.mybatis.support

import com.dbn.mybatis.plugin.BatchInsertPlugin
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.dom.OutputUtilities
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.xml.Attribute
import org.mybatis.generator.api.dom.xml.TextElement
import org.mybatis.generator.api.dom.xml.XmlElement
import org.mybatis.generator.codegen.mybatis3.ListUtilities
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator

/**
 * @author yudong
 */
class BatchInsertElementGenerator(mybatisGeneratorPlugin: BatchInsertPlugin, table: IntrospectedTable) :
    AbstractXmlElementGenerator() {
    init {
        this.introspectedTable = table
        this.context = mybatisGeneratorPlugin.contextCp
    }

    override fun addElements(parentElement: XmlElement) {
        val answer = XmlElement("insert")
        answer.addAttribute(Attribute("id", "batchInsert"))
        val parameterType = FullyQualifiedJavaType.getNewListInstance()
        answer.addAttribute(Attribute("parameterType", parameterType.fullyQualifiedName))

        context.commentGenerator.addComment(answer)

        val insertClause = StringBuilder()
        insertClause.append("insert into ")
        insertClause.append(introspectedTable.fullyQualifiedTableNameAtRuntime)
        //插入的列
        insertClause.append(" (")
        //插入的值
        val valuesClause = StringBuilder()
        valuesClause.append("(")

        //因为值不是每次循环都添加到节点上，所以用个list存起来（作用于换行）
        val valuesClauses: MutableList<String> = mutableListOf()
        val columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.allColumns)
        for (i in columns.indices) {
            val introspectedColumn = columns[i]
            insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn))
            valuesClause.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "item."))
            if (i + 1 < columns.size) {
                insertClause.append(", ")
                valuesClause.append(", ")
            }

            if (valuesClause.length > 80) {
                answer.addElement(TextElement(insertClause.toString()))
                insertClause.setLength(0)
                OutputUtilities.xmlIndent(insertClause, 1)

                valuesClauses.add(valuesClause.toString())
                valuesClause.setLength(0)
                OutputUtilities.xmlIndent(valuesClause, 1)
            }
        }

        insertClause.append(')')
        valuesClause.append(')')
        insertClause.append(" values ")
        answer.addElement(TextElement(insertClause.toString()))
        //把最后一行加上（如果没发生换行，那就是第一行）
        valuesClauses.add(valuesClause.toString())
        val foreachElement = XmlElement("foreach")
        foreachElement.addAttribute(Attribute("collection", "list"))
        foreachElement.addAttribute(Attribute("item", "item"))
        foreachElement.addAttribute(Attribute("separator", ","))

        for (clause in valuesClauses) {
            foreachElement.addElement(TextElement(clause))
        }

        answer.addElement(foreachElement)
        parentElement.addElement(answer)
    }
}
