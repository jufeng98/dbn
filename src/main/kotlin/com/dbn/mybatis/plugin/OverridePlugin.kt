package com.dbn.mybatis.plugin

import org.mybatis.generator.api.GeneratedXmlFile
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.config.Context

/**
 * @author yudong
 */
class OverridePlugin : PluginAdapter() {
    override fun setContext(context: Context) {
        super.setContext(context)
    }

    override fun validate(warnings: List<String>): Boolean {
        return true
    }

    override fun sqlMapGenerated(sqlMap: GeneratedXmlFile, introspectedTable: IntrospectedTable): Boolean {
        // 重复生成xml文件时不合并已有文件
        sqlMap.isMergeable = false

        return super.sqlMapGenerated(sqlMap, introspectedTable)
    }
}
