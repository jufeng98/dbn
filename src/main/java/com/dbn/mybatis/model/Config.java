package com.dbn.mybatis.model;

import lombok.Data;

/**
 * 界面配置
 */
@Data
public class Config {
    /**
     * 配置名称
     */
    private String name;
    /**
     * 工程目录
     */
    private String projectFolder;

    private String modelPackage;
    private String modelMvnPath;

    private String javaMapperPackage;
    private String javaMapperMvnPath;

    private String enumPostfixPackage;
    private String enumPackage;
    private String enumMvnPath;

    private String xmlFolder;
    private String xmlMvnPath;

    private boolean comment;
    private boolean overrideXML;
    private boolean overrideJava;
    private boolean needToStringHashcodeEquals;
    private boolean jpaAnnotation;
    private boolean useExample;
    private boolean generateEnum;
    private boolean integerTinyInt;
    private boolean integerBigint;
    private boolean useLombokPlugin;
    private boolean serializable;
    private boolean rowBounds;
    private boolean mapperAnnotation;
    private boolean staticFieldName;
    private boolean tkMapper;
}
