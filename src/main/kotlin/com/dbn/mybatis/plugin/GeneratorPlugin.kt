package com.dbn.mybatis.plugin

import com.dbn.utils.StringUtil
import org.mybatis.generator.api.IntrospectedColumn
import org.mybatis.generator.api.IntrospectedTable
import org.mybatis.generator.api.Plugin.ModelClassType
import org.mybatis.generator.api.PluginAdapter
import org.mybatis.generator.api.dom.java.*
import org.mybatis.generator.api.dom.xml.Attribute
import org.mybatis.generator.api.dom.xml.Document
import org.mybatis.generator.api.dom.xml.TextElement
import org.mybatis.generator.api.dom.xml.XmlElement
import org.mybatis.generator.config.Context

/**
 * @author yudong
 */
class GeneratorPlugin : PluginAdapter() {

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
        appendEqualsHashToStringMethod(topLevelClass, introspectedTable)

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable)
    }

    override fun modelFieldGenerated(
        field: Field, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: ModelClassType,
    ): Boolean {
        // 添加entity字段注释
        val docLine = """
                     /**
                      * ${introspectedColumn.remarks}
                      */
                     """.trimIndent()
        field.addJavaDocLine(docLine)

        val constStaticField = createConstField(field)
        topLevelClass.addField(constStaticField)

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType)
    }

    override fun modelGetterMethodGenerated(
        method: Method, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: ModelClassType,
    ): Boolean {
        val docLine = """
                    /**
                     * 获取${introspectedColumn.remarks}
                     */
                     """.trimIndent()
        method.addJavaDocLine(docLine)

        return super.modelGetterMethodGenerated(
            method,
            topLevelClass,
            introspectedColumn,
            introspectedTable,
            modelClassType
        )
    }

    override fun modelSetterMethodGenerated(
        method: Method, topLevelClass: TopLevelClass, introspectedColumn: IntrospectedColumn,
        introspectedTable: IntrospectedTable, modelClassType: ModelClassType,
    ): Boolean {
        // 添加entity set方法注释
        val docLine = """
                    /**
                     * 设置${introspectedColumn.remarks}
                     */
                     """.trimIndent()
        method.addJavaDocLine(docLine)

        return super.modelGetterMethodGenerated(
            method,
            topLevelClass,
            introspectedColumn,
            introspectedTable,
            modelClassType
        )
    }

    override fun modelExampleClassGenerated(
        topLevelClass: TopLevelClass,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        // 添加example类注释
        val docLine = """
                    /**
                     * 请勿手工改动此文件,请使用 mybatis generator
                     * 
                     * @author mybatis generator
                     */
                     """.trimIndent()
        topLevelClass.addJavaDocLine(docLine)

        return super.modelExampleClassGenerated(topLevelClass, introspectedTable)
    }

    override fun clientGenerated(
        interfaze: Interface,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        // 添加mapper类注释
        val docLine = """
                    /**
                     * 操纵${introspectedTable.remarks},请勿手工改动此文件,请使用 mybatis generator
                     * 
                     * @author mybatis generator
                     */
                     """.trimIndent()
        interfaze.addJavaDocLine(docLine)

        return super.clientGenerated(interfaze, introspectedTable)
    }


    override fun sqlMapDocumentGenerated(document: Document, introspectedTable: IntrospectedTable): Boolean {
        appendComment(document)

        return super.sqlMapDocumentGenerated(document, introspectedTable)
    }

    override fun sqlMapInsertElementGenerated(element: XmlElement, introspectedTable: IntrospectedTable): Boolean {
        return addGenerateKey(element, introspectedTable)
    }

    override fun sqlMapInsertSelectiveElementGenerated(
        element: XmlElement,
        introspectedTable: IntrospectedTable,
    ): Boolean {
        return addGenerateKey(element, introspectedTable)
    }

    private fun addGenerateKey(element: XmlElement, introspectedTable: IntrospectedTable): Boolean {
        val columns = introspectedTable.primaryKeyColumns
        if (columns.size == 1 && columns[0].isAutoIncrement) {
            element.addAttribute(Attribute("useGeneratedKeys", "true"))
            element.addAttribute(Attribute("keyProperty", columns[0].javaProperty))
        }

        return super.sqlMapInsertElementGenerated(element, introspectedTable)
    }

    companion object {
        fun appendEqualsHashToStringMethod(topLevelClass: TopLevelClass, introspectedTable: IntrospectedTable) {
            // 添加entity类注释
            val docLine = """
                        /**
                         * ${introspectedTable.remarks},请勿手工改动此文件,请使用 mybatis generator
                         *
                         * @author mybatis generator
                         */
                         """.trimIndent()
            topLevelClass.addJavaDocLine(docLine)

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

        fun createConstField(field: Field): Field {
            val constField =
                Field(StringUtil.wordsToConstantCase(field.name), FullyQualifiedJavaType.getStringInstance())
            constField.setInitializationString("\"" + field.name + "\"")
            constField.isTransient = false
            constField.isVolatile = false
            constField.visibility = JavaVisibility.PUBLIC
            constField.isFinal = true
            constField.isStatic = true
            return constField
        }

        fun appendComment(document: Document) {
            document.rootElement.addElement(
                0,
                TextElement("<!-- 此文件由 mybatis generator 生成,注意: 请勿手工改动此文件, 请使用 mybatis generator -->")
            )
        }
    }
}
