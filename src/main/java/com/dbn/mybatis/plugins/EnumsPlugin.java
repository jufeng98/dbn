package com.dbn.mybatis.plugins;

import com.dbn.mybatis.DbnMyBatisGenerator;
import com.dbn.mybatis.model.Config;
import com.dbn.mybatis.model.EnumMustache;
import com.dbn.mybatis.model.EnumMustacheField;
import com.dbn.utils.StringUtil;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.intellij.openapi.util.io.StreamUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumsPlugin extends PluginAdapter {
    private static final String ENUM_FLAG = "枚举";
    private final MustacheFactory mf = new DefaultMustacheFactory();
    private Config config;

    @Override
    public void setContext(Context context) {
        config = DbnMyBatisGenerator.getConfig();
        super.setContext(context);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @SneakyThrows
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        val remark = introspectedColumn.getRemarks();

        if (remark.contains(ENUM_FLAG)) {
            generateEnum(field, remark);
        }

        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn,
                introspectedTable, modelClassType);
    }

    private void generateEnum(Field field, String remark) throws Exception {
        String name = StringUtil.toCamelCase(field.getName());
        name = StringUtil.capitalizeFirstWord2(name);
        String enumClassName = name + config.getEnumPostfixPackage();
        String enumValue = remark.split("-")[1];
        String[] enums = enumValue.split(";");

        List<EnumMustacheField> list = Arrays.stream(enums)
                .map(s -> {
                    val tmpList = s.split(":");
                    val enumMustacheField = new EnumMustacheField();
                    enumMustacheField.label = tmpList[0];
                    enumMustacheField.setCode(Integer.parseInt(tmpList[1]));
                    enumMustacheField.msg = tmpList[2];
                    return enumMustacheField;
                }).collect(Collectors.toList());

        EnumMustache enumMustache = new EnumMustache();
        enumMustache.enumPath = config.getEnumPackage();
        enumMustache.enumClassName = enumClassName;
        enumMustache.enumFields = list;

        String targetProject = config.getProjectFolder() + "/" + config.getEnumMvnPath();
        File directory = new DefaultShellCallback(true).getDirectory(targetProject, config.getEnumPackage());
        File targetFile = new File(directory, enumClassName + ".java");

        URL url = getClass().getClassLoader().getResource("mybatis/Enum.mustache");
        @Cleanup
        @SuppressWarnings("DataFlowIssue")
        InputStream inputStream = url.openStream();
        String str = new String(StreamUtil.readBytes(inputStream), StandardCharsets.UTF_8);
        Mustache mustache = mf.compile(new StringReader(str), "Enum.mustache");

        mustache.execute(new PrintWriter(targetFile), enumMustache).flush();
    }
}
