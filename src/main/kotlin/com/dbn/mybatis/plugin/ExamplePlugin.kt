package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.TopLevelClass

/**
 * @author yudong
 */
class ExamplePlugin : PluginAdapter() {

    override fun validate(warnings: List<String>): Boolean {
        return true
    }

    override fun modelExampleClassGenerated(
        topLevelClass: TopLevelClass?,
        introspectedTable: IntrospectedTable?,
    ): Boolean {
        return false
    }
}