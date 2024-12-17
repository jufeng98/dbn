package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Interface

class MapperAnnotationPlugin : PluginAdapter() {

    override fun validate(warnings: MutableList<String>): Boolean {
        return true
    }

    override fun clientGenerated(
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        interfaze.addAnnotation("@org.apache.ibatis.annotations.Mapper")

        return super.clientGenerated(interfaze, introspectedTable)
    }
}