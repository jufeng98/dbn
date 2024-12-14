package com.dbn.mybatis.plugin

import com.dbn.mybatis.model.EnumMustache
import com.dbn.mybatis.model.EnumMustacheField
import com.dbn.utils.StringUtil
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin.ModelClassType
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.Field
import org.mybatis.generator.api.dom.java.TopLevelClass
import org.mybatis.generator.config.Context
import org.mybatis.generator.internal.DefaultShellCallback
import java.io.File
import java.io.PrintWriter
import java.io.StringReader
import java.nio.charset.StandardCharsets

/**
 * 根据注释生成枚举类,注释格式必须满足如下所示:<br>
 * 性别,枚举-MALE:1:男;FEMALE:2:女
 *
 * @author yudong
 */
class EnumsPlugin : PluginAdapter() {
    private val mf: MustacheFactory = DefaultMustacheFactory()

    private lateinit var enumPath: String
    private lateinit var targetProject: String

    override fun setContext(context: Context) {
        enumPath = ""
        targetProject = context.javaModelGeneratorConfiguration.targetProject
    }

    override fun validate(warnings: List<String>): Boolean {
        return true
    }

    override fun modelFieldGenerated(
        field: Field, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: ModelClassType,
    ): Boolean {
        val remark = introspectedColumn.remarks

        if (remark.contains(ENUM_FLAG)) {
            generateEnum(field, remark)
        }

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)
    }

    private fun generateEnum(field: Field, remark: String) {
        // 性别,枚举-MALE:1:男;FEMALE:2:女
        val enumClassName = StringUtil.toCamelCase(field.name) + "Enum"
        val enumValue = remark.split("-")[1]
        val enums = enumValue.split(";")

        val list = enums
            .map { s: String ->
                val tmpList = s.split(":")
                val enumMustacheField = EnumMustacheField()
                enumMustacheField.label = tmpList[0]
                enumMustacheField.code = tmpList[1].toInt()
                enumMustacheField.msg = tmpList[2]
                enumMustacheField
            }

        val enumMustache = EnumMustache()
        enumMustache.enumPath = enumPath
        enumMustache.enumClassName = enumClassName
        enumMustache.enumFields = list

        val directory = DefaultShellCallback(true).getDirectory(targetProject, enumPath)
        val targetFile = File(directory, "$enumClassName.java")

        val url = javaClass.classLoader.getResource("mybatis/Enum.mustache")
        val str = url.readText(StandardCharsets.UTF_8)
        val mustache = mf.compile(StringReader(str), "Enum.mustache")

        mustache.execute(PrintWriter(targetFile), enumMustache).flush()
    }


    companion object {
        private const val ENUM_FLAG = "枚举"
    }
}
