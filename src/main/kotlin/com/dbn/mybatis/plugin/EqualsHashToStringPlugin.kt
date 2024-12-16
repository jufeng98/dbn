package com.dbn.mybatis.plugin

import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.*
import org.mybatis.generator.config.Context

class EqualsHashToStringPlugin : PluginAdapter() {

    override fun setContext(context: Context) {
        super.setContext(context)
    }

    override fun validate(warnings: List<String>): Boolean {
        return true
    }

    override fun modelBaseRecordClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        appendEqualsHashToStringMethod(topLevelClass)

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)
    }


    private fun appendEqualsHashToStringMethod(topLevelClass: TopLevelClass) {
        topLevelClass.addImportedType("org.apache.commons.lang3.builder.EqualsBuilder")
        topLevelClass.addImportedType("org.apache.commons.lang3.builder.HashCodeBuilder")
        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringBuilder")
        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringStyle")

        val newMethods: MutableList<Method> = ArrayList()

        newMethods.add(toStringMethod())

        newMethods.add(equalsMethod())

        newMethods.add(hashMethod())

        newMethods.addAll(topLevelClass.methods)

        val field = InnerClass::class.java.getDeclaredField("methods")
        field.isAccessible = true
        field[topLevelClass] = newMethods
    }

    private fun toStringMethod(): Method {
        val toStringMethod = Method("toString")
        toStringMethod.addAnnotation("@Override")
        toStringMethod.visibility = JavaVisibility.PUBLIC
        toStringMethod.setReturnType(FullyQualifiedJavaType.getStringInstance())
        toStringMethod.addBodyLine("return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);")
        return toStringMethod
    }

    private fun equalsMethod(): Method {
        val equalsMethod = Method("equals")
        equalsMethod.addAnnotation("@Override")
        equalsMethod.visibility = JavaVisibility.PUBLIC
        equalsMethod.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance())
        equalsMethod.addParameter(0, Parameter(FullyQualifiedJavaType.getObjectInstance(), "obj"))
        equalsMethod.addBodyLine("return EqualsBuilder.reflectionEquals(this, obj);")
        return equalsMethod
    }

    private fun hashMethod(): Method {
        val hashMethod = Method("hashCode")
        hashMethod.addAnnotation("@Override")
        hashMethod.visibility = JavaVisibility.PUBLIC
        hashMethod.setReturnType(FullyQualifiedJavaType.getIntInstance())
        hashMethod.addBodyLine("return HashCodeBuilder.reflectionHashCode(this);")
        return hashMethod
    }
}
