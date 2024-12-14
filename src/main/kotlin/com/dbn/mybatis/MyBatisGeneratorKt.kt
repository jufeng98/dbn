package com.dbn.mybatis

import com.dbn.connection.config.ConnectionSettings
import org.mybatis.generator.api.MyBatisGenerator
import org.mybatis.generator.config.xml.ConfigurationParser
import org.mybatis.generator.internal.DefaultShellCallback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.ls.DOMImplementationLS
import org.xml.sax.InputSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.StringReader
import java.nio.charset.StandardCharsets
import java.sql.DriverManager
import java.sql.Types
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/**
 * 生成mybatis model,mapper等文件
 *
 * @author yudong
 */
class MyBatisGeneratorKt {

    fun generator(connectionSettings: ConnectionSettings, tableName: String) {
        val properties = Properties()
        properties.setProperty("targetRuntime", "MyBatis3")
        properties.setProperty("delimiter", "`")

        val settings = connectionSettings.databaseSettings
        properties.setProperty("driverClass", settings.driver)
        properties.setProperty("connectionURL", settings.connectionUrl)
        properties.setProperty("userId", settings.authenticationInfo.user)
        properties.setProperty("password", settings.authenticationInfo.password)

        val content = readAndModifyConfigFile(connectionSettings, properties, tableName)
        logger.warn("xml配置文件内容:\r\n{}", content)

        val warnings: MutableList<String> = mutableListOf()
        val cp = ConfigurationParser(properties, warnings)
        val config = cp.parseConfiguration(StringReader(content))

        val myBatisGenerator = MyBatisGenerator(config, DefaultShellCallback(true), warnings)
        myBatisGenerator.generate(null)

        logger.warn("生成情况:")
        warnings.forEach(Consumer { x -> logger.warn(x) })
    }

    private fun readAndModifyConfigFile(
        connectionSettings: ConnectionSettings,
        properties: Properties,
        tableName: String,
    ): String {
        val str = javaClass.classLoader.getResource("mybatis/generatorConfig.xml").readText(StandardCharsets.UTF_8)
        val matcher = VARIABLE_PATTERN.matcher(str)

        val content = matcher.replaceAll {
            val matchStr = it.group()
            val variable = matchStr.substring(2, matchStr.length - 1)
            properties.getProperty(variable)
        }

        val doc = FACTORY.newDocumentBuilder().parse(InputSource(ByteArrayInputStream(content.toByteArray())))
        val rootEle = doc.documentElement
        val xPath = X_PATH_FACTORY.newXPath()

        val parentNode = xPath.evaluate("/generatorConfiguration/context", rootEle, XPathConstants.NODE) as Node

        val nodeList =
            xPath.evaluate("/generatorConfiguration/context/table", rootEle, XPathConstants.NODESET) as NodeList
        for (i in 0 until nodeList.length) {
            parentNode.removeChild(nodeList.item(i))
        }

        val element = doc.createElement("table")
        element.setAttribute("tableName", tableName)
        element.setAttribute("schema", connectionSettings.databaseSettings.databaseInfo.database)
        parentNode.appendChild(element)

        handlerAutoIncrement(xPath, doc, connectionSettings)

        changeTinyintToInteger(xPath, doc, connectionSettings)

        val domImplementationLs = doc.implementation.getFeature("LS", "3.0") as DOMImplementationLS

        ByteArrayOutputStream().use {
            val lsOutput = domImplementationLs.createLSOutput()
            lsOutput.encoding = "UTF-8"
            lsOutput.byteStream = it

            val lsSerializer = domImplementationLs.createLSSerializer()
            lsSerializer.write(doc, lsOutput)

            return String(it.toByteArray(), StandardCharsets.UTF_8)
        }
    }

    /**
     * 修改所有db列类型为TINYINT的,生成到Java里用Integer表示,通过修改table节点来完成这个任务
     */
    private fun changeTinyintToInteger(xPath: XPath, doc: Document, connectionSettings: ConnectionSettings) {
        val settings = connectionSettings.databaseSettings
        val rootEle = doc.documentElement
        DriverManager.getConnection(
            settings.connectionUrl, settings.authenticationInfo.user, settings.authenticationInfo.password
        ).use { connection ->
            val nodeList = xPath.evaluate("//table", rootEle, XPathConstants.NODESET) as NodeList
            for (i in 0 until nodeList.length) {
                val tableEle = (nodeList.item(i) as Element)
                if (tableEle.getElementsByTagName("columnOverride").length != 0) {
                    continue
                }

                val tableName = tableEle.getAttribute("tableName")
                val databaseMetaData = connection.metaData
                val rs = databaseMetaData.getColumns(null, null, tableName, null)
                while (rs.next()) {
                    val dataType = rs.getInt("DATA_TYPE")
                    if (Types.TINYINT != dataType && Types.BIT != dataType) {
                        continue
                    }

                    val columnName = rs.getString("COLUMN_NAME")
                    val columnOverrideEle = doc.createElement("columnOverride")
                    columnOverrideEle.setAttribute("column", columnName)
                    columnOverrideEle.setAttribute("javaType", "Integer")
                    columnOverrideEle.setAttribute("jdbcType", "INTEGER")
                    tableEle.appendChild(columnOverrideEle)
                }
            }
        }
    }

    /**
     * 处理自动递增主键
     */
    private fun handlerAutoIncrement(xPath: XPath, doc: Document, connectionSettings: ConnectionSettings) {
        val settings = connectionSettings.databaseSettings
        val rootEle = doc.documentElement
        DriverManager.getConnection(
            settings.connectionUrl, settings.authenticationInfo.user, settings.authenticationInfo.password
        ).use { connection ->
            val tableNodeList = xPath.evaluate("//table", rootEle, XPathConstants.NODESET) as NodeList
            for (i in 0 until tableNodeList.length) {
                val tableEle = (tableNodeList.item(i) as Element)
                val tableName = tableEle.getAttribute("tableName")
                val databaseMetaData = connection.metaData

                var supportsIsAutoIncrement = false
                val resultSet = databaseMetaData.getColumns(null, null, tableName, "%")
                val metaData = resultSet.metaData
                for (j in 1..metaData.columnCount) {
                    if ("IS_AUTOINCREMENT" == metaData.getColumnName(j)) {
                        supportsIsAutoIncrement = true
                        break
                    }
                }

                if (!supportsIsAutoIncrement) {
                    return
                }

                val tableResultSet = databaseMetaData.getPrimaryKeys(null, null, tableName)
                while (tableResultSet.next()) {
                    val columnName = tableResultSet.getString("COLUMN_NAME")
                    val generatedKeyEle = doc.createElement("generatedKey")
                    generatedKeyEle.setAttribute("column", columnName)
                    generatedKeyEle.setAttribute("sqlStatement", "MySql")
                    generatedKeyEle.setAttribute("identity", "true")
                    tableEle.appendChild(generatedKeyEle)
                }
            }
        }
    }

    companion object {
        val VARIABLE_PATTERN: Pattern = Pattern.compile("\\$\\{[^{}]+}")
        private val logger: Logger = LoggerFactory.getLogger(MyBatisGeneratorKt::class.java)

        private val FACTORY: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        private val X_PATH_FACTORY: XPathFactory = XPathFactory.newInstance()

        init {
            FACTORY.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        }
    }
}
