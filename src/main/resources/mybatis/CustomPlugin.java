package com.dbn.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * Customize MyBatis Generator
 * @see <a href="https://mybatis.org/generator/reference/pluggingIn.html">PluginAdapter Official documents</a>
 * @noinspection unused
 */
public class CustomPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {        
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

}
