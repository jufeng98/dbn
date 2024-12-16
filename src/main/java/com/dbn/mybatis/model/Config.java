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

    /**
     * 是否生成实体注释（来自表）
     */
    private boolean comment;

    /**
     * 是否覆盖原xml
     */
    private boolean overrideXML;

    /**
     * 是否覆盖原java
     */
    private boolean overrideJava;

    /**
     * 是否生成toString/hashCode/equals方法
     */
    private boolean needToStringHashcodeEquals;

    /**
     * 是否生成JPA注解
     */
    private boolean jpaAnnotation;

    /**
     * 是否使用Example
     */
    private boolean useExample;
    private boolean generateEnum;
    private boolean integerTinyInt;
    private boolean useLombokPlugin;
    private boolean serializable;
    private boolean rowBounds;
    private boolean mapperAnnotation;
    private boolean staticFieldName;
}
