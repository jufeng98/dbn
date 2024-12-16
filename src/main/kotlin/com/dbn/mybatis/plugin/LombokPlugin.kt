package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.AbstractJavaType
import org.mybatis.generator.api.dom.java.Method
import org.mybatis.generator.api.dom.java.TopLevelClass


class LombokPlugin : PluginAdapter() {

    override fun validate(warnings: List<String?>?): Boolean {
        return true
    }

    override fun modelBaseRecordClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        addLombok(topLevelClass)

        delGetSetMethods(topLevelClass)

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)
    }

    private fun delGetSetMethods(topLevelClass: TopLevelClass) {
        val field = AbstractJavaType::class.java.getDeclaredField("methods")
        field.isAccessible = true

        val newMethods = mutableListOf<Method>()
        topLevelClass.methods.forEach {
            if (it.name.startsWith("get") || it.name.startsWith("set")) {
                return@forEach
            }

            newMethods.add(it)
        }

        field[topLevelClass] = newMethods
    }

    private fun addLombok(topLevelClass: TopLevelClass) {
        topLevelClass.addImportedType("lombok.AllArgsConstructor")
        topLevelClass.addImportedType("lombok.Builder")
        topLevelClass.addImportedType("lombok.Data")
        topLevelClass.addImportedType("lombok.NoArgsConstructor")

        topLevelClass.addAnnotation("@Data")
        topLevelClass.addAnnotation("@Builder")
        topLevelClass.addAnnotation("@AllArgsConstructor")
        topLevelClass.addAnnotation("@NoArgsConstructor")
    }

}
