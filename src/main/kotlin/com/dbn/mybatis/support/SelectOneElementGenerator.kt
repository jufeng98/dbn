package com.dbn.mybatis.support

import com.dbn.mybatis.plugin.SelectOnePlugin
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.dom.xml.Attribute
import org.mybatis.generator.api.dom.xml.TextElement
import org.mybatis.generator.api.dom.xml.XmlElement
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator
import org.mybatis.generator.internal.util.StringUtility

/**
 * @author yudong
 */
class SelectOneElementGenerator(plugin: SelectOnePlugin, table: IntrospectedTable) : AbstractXmlElementGenerator() {
    init {
        this.introspectedTable = table
        this.context = plugin.contextCp
    }

    override fun addElements(parentElement: XmlElement) {
        val answer = XmlElement("select")
        val exampleType = introspectedTable.exampleType

        answer.addAttribute(Attribute("id", "selectOneByExample"))
        answer.addAttribute(Attribute("resultMap", introspectedTable.baseResultMapId))
        answer.addAttribute(Attribute("parameterType", exampleType))

        context.commentGenerator.addComment(answer)

        answer.addElement(TextElement("select"))
        var ifElement = XmlElement("if")
        ifElement.addAttribute(Attribute("test", "distinct"))
        ifElement.addElement(TextElement("distinct"))
        answer.addElement(ifElement)

        val sb = StringBuilder()
        if (StringUtility.stringHasValue(introspectedTable.selectByExampleQueryId)) {
            sb.append('\'')
            sb.append(introspectedTable.selectByExampleQueryId)
            sb.append("' as QUERYID,")
            answer.addElement(TextElement(sb.toString()))
        }
        answer.addElement(baseColumnListElement)

        sb.setLength(0)
        sb.append("from ")
        sb.append(introspectedTable.aliasedFullyQualifiedTableNameAtRuntime)
        answer.addElement(TextElement(sb.toString()))
        answer.addElement(exampleIncludeElement)

        ifElement = XmlElement("if")
        ifElement.addAttribute(Attribute("test", "orderByClause != null"))
        ifElement.addElement(TextElement("order by \${orderByClause}"))

        answer.addElement(ifElement)
        answer.addElement(TextElement("limit 1"))
        parentElement.addElement(answer)
    }
}
