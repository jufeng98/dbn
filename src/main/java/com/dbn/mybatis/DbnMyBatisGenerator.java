package com.dbn.mybatis;

import com.dbn.common.database.AuthenticationInfo;
import com.dbn.compile.MyClassLoader;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.info.ConnectionInfo;
import com.dbn.driver.DatabaseDriverManager;
import com.dbn.mybatis.custom.CustomPluginHandler;
import com.dbn.mybatis.model.Config;
import com.dbn.mybatis.plugin.BatchInsertPlugin;
import com.dbn.mybatis.plugin.CommentPlugin;
import com.dbn.mybatis.plugin.EqualsHashToStringPlugin;
import com.dbn.mybatis.plugin.ExamplePlugin;
import com.dbn.mybatis.plugin.JpaAnnotationPlugin;
import com.dbn.mybatis.plugin.LombokPlugin;
import com.dbn.mybatis.plugin.MapperAnnotationPlugin;
import com.dbn.mybatis.plugin.SelectOnePlugin;
import com.dbn.mybatis.plugin.SerializablePlugin;
import com.dbn.mybatis.plugin.StaticFieldNamePlugin;
import com.dbn.mybatis.plugin.TkMapperPlugin;
import com.dbn.mybatis.plugins.EnumsPlugin;
import com.dbn.mybatis.ui.CustomPluginEditorDialog;
import com.dbn.object.DBTable;
import com.dbn.utils.NotifyUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.StreamUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFileManager;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.plugins.RowBoundsPlugin;
import org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DbnMyBatisGenerator {
    Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{[^{}]+}");
    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
    private static final XPathFactory X_PATH_FACTORY = XPathFactory.newInstance();
    private static DbnMyBatisGenerator dbnMyBatisGenerator;

    static {
        try {
            //noinspection HttpUrlsUsage
            FACTORY.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private final DBTable dbTable;
    private final Config config;

    private final ConnectionHandler connectionHandler;
    private final ConnectionInfo connectionInfo;
    private final AuthenticationInfo authenticationInfo;

    public DbnMyBatisGenerator(DBTable dbTable, Config config) {
        this.dbTable = dbTable;
        this.config = config;

        connectionHandler = dbTable.getConnection();
        connectionInfo = connectionHandler.getConnectionInfo();
        authenticationInfo = connectionHandler.getAuthenticationInfo();
    }

    public void generator() throws Exception {
        Properties properties = createProperties();

        String content = readAndModifyConfigFile(properties);
        log.warn("xml配置文件内容:\r\n{}", content);

        List<String> warnings = Lists.newArrayList();
        ConfigurationParser cp = new ConfigurationParser(properties, warnings);
        Configuration configuration = cp.parseConfiguration(new StringReader(content));

        Context context = addPlugins(configuration);

        Project project = dbTable.getProject();
        MyDefaultShellCallback shellCallback = new MyDefaultShellCallback(true, project);
        MyProgressCallback progressCallback = new MyProgressCallback(shellCallback);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, shellCallback, warnings);

        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();

        ClassLoader classLoader = getClassLoader(project, context);

        try {
            Thread.currentThread().setContextClassLoader(classLoader);

            myBatisGenerator.generate(progressCallback);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);

            if (classLoader instanceof MyClassLoader myClassLoader) {
                myClassLoader.close();
            }
        }

        VirtualFileManager.getInstance().asyncRefresh(() ->
                LocalFileSystem.getInstance().refreshFiles(progressCallback.getVirtualFiles(), true, false,
                        progressCallback::reformatCode));

        NotifyUtil.INSTANCE.notifySuccess(project, "生成情况:" + String.join("、", warnings));

        dbnMyBatisGenerator = null;
    }

    public static DbnMyBatisGenerator createInstance(DBTable dbTable, Config config) {
        if (dbnMyBatisGenerator == null) {
            dbnMyBatisGenerator = new DbnMyBatisGenerator(dbTable, config);
        }

        return dbnMyBatisGenerator;
    }

    private ClassLoader getClassLoader(Project project, Context context) {
        String javaCode = CustomPluginEditorDialog.getCustomPluginCode(project);
        if (javaCode == null) {
            return DatabaseDriverManager.getClassLoader();
        }

        try {
            CustomPluginHandler customPluginHandler = new CustomPluginHandler(project);
            MyClassLoader myClassLoader = customPluginHandler.compile(javaCode);

            PluginConfiguration pluginConfiguration = new PluginConfiguration();
            pluginConfiguration.setConfigurationType(CustomPluginHandler.PLUGIN_CLASS_NAME);
            context.addPluginConfiguration(pluginConfiguration);

            return myClassLoader;
        } catch (RuntimeException e) {
            NotifyUtil.INSTANCE.notifyWarn(project, CustomPluginHandler.PLUGIN_SIMPLE_NAME + " was ignored!", e.getMessage());

            return DatabaseDriverManager.getClassLoader();
        }
    }

    public static Config getConfig() {
        return dbnMyBatisGenerator.config;
    }

    private Properties createProperties() {
        Properties properties = new Properties();
        properties.setProperty("targetRuntime", "MyBatis3");

        properties.setProperty("delimiter", "`");

        properties.setProperty("driverClass", connectionInfo.getDatabaseType().getDriverClassName());
        properties.setProperty("connectionURL", connectionInfo.getUrl());
        properties.setProperty("userId", authenticationInfo.getUser());
        properties.setProperty("password", authenticationInfo.getPassword());

        properties.setProperty("projectFolder", config.getProjectFolder());
        properties.setProperty("modelPackage", config.getModelPackage());
        properties.setProperty("modelMvnPath", config.getModelMvnPath());
        properties.setProperty("xmlFolder", config.getXmlFolder());
        properties.setProperty("xmlMvnPath", config.getXmlMvnPath());
        properties.setProperty("javaMapperPackage", config.getJavaMapperPackage());
        properties.setProperty("javaMapperMvnPath", config.getJavaMapperMvnPath());

        return properties;
    }

    private Context addPlugins(Configuration configuration) {
        Context context = getContext(configuration, "MySQL");
        if (config.isOverrideXML()) {
            addPlugin(context, UnmergeableXmlMappersPlugin.class);
        }

        if (config.isTkMapper()) {
            addPlugin(context, TkMapperPlugin.class);
        }

        if (config.isUseLombokPlugin()) {
            addPlugin(context, LombokPlugin.class);
        }

        if (config.isJpaAnnotation()) {
            addPlugin(context, JpaAnnotationPlugin.class);
        }

        if (config.isGenerateEnum()) {
            addPlugin(context, EnumsPlugin.class);
        }

        if (config.isRowBounds()) {
            addPlugin(context, RowBoundsPlugin.class);
        }

        if (config.isMapperAnnotation()) {
            addPlugin(context, MapperAnnotationPlugin.class);
        }

        if (config.isUseExample()) {
            addPlugin(context, SelectOnePlugin.class);
            addPlugin(context, BatchInsertPlugin.class);
        } else {
            addPlugin(context, ExamplePlugin.class);
        }

        if (config.isStaticFieldName()) {
            addPlugin(context, StaticFieldNamePlugin.class);
        }

        if (config.isComment()) {
            addPlugin(context, CommentPlugin.class);
        }

        if (config.isSerializable()) {
            addPlugin(context, SerializablePlugin.class);
        }

        if (config.isNeedToStringHashcodeEquals()) {
            addPlugin(context, EqualsHashToStringPlugin.class);
        }

        return context;
    }

    private String readAndModifyConfigFile(Properties properties) throws Exception {
        URL url = getClass().getClassLoader().getResource("mybatis/generatorConfig.xml");

        @SuppressWarnings("DataFlowIssue")
        @Cleanup
        InputStream inputStream = url.openStream();
        String str = new String(StreamUtil.readBytes(inputStream));
        Matcher matcher = VARIABLE_PATTERN.matcher(str);

        String content = matcher.replaceAll(it -> {
            String matchStr = it.group();
            String variable = matchStr.substring(2, matchStr.length() - 1);
            String value = properties.getProperty(variable);
            //noinspection deprecation
            return StringEscapeUtils.escapeXml(StringUtils.defaultString(value));
        });

        Document doc = FACTORY.newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))));
        Element rootEle = doc.getDocumentElement();
        XPath xPath = X_PATH_FACTORY.newXPath();

        Node parentNode = (Node) xPath.evaluate("/generatorConfiguration/context", rootEle, XPathConstants.NODE);

        NodeList nodeList = (NodeList) xPath.evaluate("/generatorConfiguration/context/table", rootEle, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            parentNode.removeChild(nodeList.item(i));
        }

        Element element = doc.createElement("table");
        element.setAttribute("tableName", dbTable.getName());
        element.setAttribute("schema", connectionHandler.getDatabaseInfo().getDatabase());
        parentNode.appendChild(element);

        handlerAutoIncrement(xPath, doc);

        changeJavaFieldType(xPath, doc);

        DOMImplementationLS domImplementationLs = (DOMImplementationLS) doc.getImplementation().getFeature("LS", "3.0");

        @Cleanup
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        LSOutput lsOutput = domImplementationLs.createLSOutput();
        lsOutput.setEncoding("UTF-8");
        lsOutput.setByteStream(outputStream);

        LSSerializer lsSerializer = domImplementationLs.createLSSerializer();
        lsSerializer.write(doc, lsOutput);

        return outputStream.toString(StandardCharsets.UTF_8);
    }

    private void addPlugin(Context context, Class<?> clz) {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType(clz.getName());
        context.addPluginConfiguration(pluginConfiguration);
    }

    private Context getContext(Configuration configuration, @SuppressWarnings("SameParameterValue") String id) {
        for (Context context : configuration.getContexts()) {
            if (id.equals(context.getId())) {
                return context;
            }
        }
        throw new UnsupportedOperationException();
    }


    /**
     * 处理自动递增主键
     */
    private void handlerAutoIncrement(XPath xPath, Document doc) throws Exception {
        Element rootEle = doc.getDocumentElement();

        NodeList tableNodeList = (NodeList) xPath.evaluate("//table", rootEle, XPathConstants.NODESET);
        for (int i = 0; i < tableNodeList.getLength(); i++) {
            Element tableEle = (Element) tableNodeList.item(i);
            String tableName = tableEle.getAttribute("tableName");
            DatabaseMetaData databaseMetaData = connectionHandler.getMainConnection().getMetaData();

            var supportsIsAutoIncrement = false;
            ResultSet resultSet = databaseMetaData.getColumns(null, null, tableName, "%");
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int j = 1; j <= metaData.getColumnCount(); j++) {
                if ("IS_AUTOINCREMENT".equals(metaData.getColumnName(j))) {
                    supportsIsAutoIncrement = true;
                    break;
                }
            }

            if (!supportsIsAutoIncrement) {
                return;
            }

            ResultSet tableResultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);
            while (tableResultSet.next()) {
                String columnName = tableResultSet.getString("COLUMN_NAME");
                Element generatedKeyEle = doc.createElement("generatedKey");
                generatedKeyEle.setAttribute("column", columnName);
                generatedKeyEle.setAttribute("sqlStatement", "MySql");
                generatedKeyEle.setAttribute("identity", "true");
                tableEle.appendChild(generatedKeyEle);
            }
        }
    }


    /**
     * 修改所有db列类型为TINYINT或者BIGINT的,生成到Java里用Integer表示
     */
    private void changeJavaFieldType(XPath xPath, Document doc) throws Exception {
        Element rootEle = doc.getDocumentElement();
        NodeList nodeList = (NodeList) xPath.evaluate("//table", rootEle, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element tableEle = (Element) nodeList.item(i);
            if (tableEle.getElementsByTagName("columnOverride").getLength() != 0) {
                continue;
            }

            String tableName = tableEle.getAttribute("tableName");
            DatabaseMetaData databaseMetaData = connectionHandler.getMainConnection().getMetaData();
            ResultSet rs = databaseMetaData.getColumns(null, null, tableName, null);
            while (rs.next()) {
                int dataType = rs.getInt("DATA_TYPE");
                if (Types.TINYINT == dataType || Types.BIT == dataType) {
                    if (config.isIntegerTinyInt()) {
                        String columnName = rs.getString("COLUMN_NAME");
                        Element columnOverrideEle = doc.createElement("columnOverride");
                        columnOverrideEle.setAttribute("column", columnName);
                        columnOverrideEle.setAttribute("javaType", "Integer");
                        columnOverrideEle.setAttribute("jdbcType", "INTEGER");
                        tableEle.appendChild(columnOverrideEle);
                    }
                } else if (Types.BIGINT == dataType) {
                    if (config.isIntegerBigint()) {
                        String columnName = rs.getString("COLUMN_NAME");
                        Element columnOverrideEle = doc.createElement("columnOverride");
                        columnOverrideEle.setAttribute("column", columnName);
                        columnOverrideEle.setAttribute("javaType", "Integer");
                        columnOverrideEle.setAttribute("jdbcType", "BIGINT");
                        tableEle.appendChild(columnOverrideEle);
                    }
                }
            }
        }
    }

}
