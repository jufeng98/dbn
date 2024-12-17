package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType
import org.mybatis.generator.api.dom.java.Interface
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.TopLevelClass
import org.mybatis.generator.api.dom.xml.XmlElement

/**
 * @author yudong
 */
class TkMapperPlugin : PluginAdapter() {

    override fun validate(warnings: List<String>): Boolean {
        return true
    }

    override fun clientGenerated(interfaze: Interface, introspectedTable: IntrospectedTable): Boolean {
        val entityType = FullyQualifiedJavaType(introspectedTable.baseRecordType)

        val mapper = "tk.mybatis.mapper.common.Mapper"
        interfaze.addImportedType(FullyQualifiedJavaType(mapper))
        interfaze.addSuperInterface(FullyQualifiedJavaType(mapper + "<" + entityType.shortName + ">"))

        interfaze.addImportedType(entityType)
        return true
    }

    override fun clientDeleteByPrimaryKeyMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun clientInsertMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun clientInsertSelectiveMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun clientSelectAllMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun clientSelectByPrimaryKeyMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun clientUpdateByPrimaryKeySelectiveMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(
        method: Method,
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun sqlMapDeleteByPrimaryKeyElementGenerated(
        element: XmlElement,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun sqlMapInsertElementGenerated(element: XmlElement, introspectedTable: IntrospectedTable): Boolean {
        return false
    }

    override fun sqlMapInsertSelectiveElementGenerated(
        element: XmlElement,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun sqlMapSelectAllElementGenerated(element: XmlElement, introspectedTable: IntrospectedTable): Boolean {
        return false
    }

    override fun sqlMapSelectByPrimaryKeyElementGenerated(
        element: XmlElement,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun sqlMapUpdateByPrimaryKeySelectiveElementGenerated(
        element: XmlElement,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(
        element: XmlElement,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
        element: XmlElement,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun providerGenerated(topLevelClass: TopLevelClass, introspectedTable: IntrospectedTable): Boolean {
        return false
    }

    override fun providerApplyWhereMethodGenerated(
        method: Method,
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun providerInsertSelectiveMethodGenerated(
        method: Method,
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun providerUpdateByPrimaryKeySelectiveMethodGenerated(
        method: Method,
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return false
    }

    override fun clientCountByExampleMethodGenerated(
        method: Method?,
        interfaze: Interface?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun sqlMapCountByExampleElementGenerated(
        element: XmlElement?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun clientDeleteByExampleMethodGenerated(
        method: Method?,
        interfaze: Interface?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun sqlMapDeleteByExampleElementGenerated(
        element: XmlElement?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun clientSelectByExampleWithoutBLOBsMethodGenerated(
        method: Method?,
        interfaze: Interface?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun sqlMapSelectByExampleWithoutBLOBsElementGenerated(
        element: XmlElement?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun clientUpdateByExampleSelectiveMethodGenerated(
        method: Method?,
        interfaze: Interface?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun sqlMapUpdateByExampleSelectiveElementGenerated(
        element: XmlElement?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun clientUpdateByExampleWithoutBLOBsMethodGenerated(
        method: Method?,
        interfaze: Interface?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun sqlMapUpdateByExampleWithoutBLOBsElementGenerated(
        element: XmlElement?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun sqlMapExampleWhereClauseElementGenerated(
        element: XmlElement?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun sqlMapUpdateByExampleWithBLOBsElementGenerated(
        element: XmlElement?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }

    override fun sqlMapBaseColumnListElementGenerated(
        element: XmlElement?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }
}