package com.dbn.sql.highlight

import com.dbn.sql.SqlIcons
import com.dbn.sql.SqlLanguage
import com.google.common.collect.ImmutableMap
import com.intellij.lang.Language
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.RainbowColorSettingsPage
import javax.swing.Icon

/**
 * @author yudong
 */
class SqlColorSettingsPage : RainbowColorSettingsPage {
    private val stringKey = "string"
    private val ourAdditionalHighlighting: Map<String, TextAttributesKey> =
        ImmutableMap.of(stringKey, SqlSyntaxHighlighter.STRING)

    private val attributeDescriptors = mutableListOf<AttributesDescriptor>()

    init {
        attributeDescriptors.add(AttributesDescriptor("Comment", SqlSyntaxHighlighter.COMMENT))
        attributeDescriptors.add(AttributesDescriptor("String", SqlSyntaxHighlighter.STRING))
        attributeDescriptors.add(AttributesDescriptor("Number", SqlSyntaxHighlighter.NUMBER))
        attributeDescriptors.add(AttributesDescriptor("Identifier", SqlSyntaxHighlighter.IDENTIFIER))
        attributeDescriptors.add(AttributesDescriptor("Keyword", SqlSyntaxHighlighter.KEYWORD))
    }

    override fun getIcon(): Icon {
        return SqlIcons.FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return SqlSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """
            /*
             * Name: GitFlowPlus
             * Version: 2.0
             * SQL demo text
             */
            
            CREATE TABLE mall_sys_dict_entry (
              DICTTYPEID varchar(50) NOT NULL COMMENT <$stringKey>'业务字典子选项'</$stringKey>,
              DICTID varchar(100) NOT NULL COMMENT <$stringKey>'业务字典子选项编号'</$stringKey>,
              DICTNAME varchar(200) DEFAULT NULL COMMENT '业务字典子选项名称',
              STATUS int(10) DEFAULT NULL COMMENT '状态（1使用中/0已废弃）',
              SORTNO int(10) DEFAULT NULL COMMENT '排序编码',
              PRIMARY KEY (DICTTYPEID,DICTID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典表';
                              
            -- insert data
            INSERT INTO
              mall_sys_dict_entry (DICTTYPEID, DICTID, DICTNAME, STATUS, SORTNO)
            VALUES
              ('ACTION_TYPE', '1', '沟通提报', 1, 1);
           
            -- select data
            select
                msde.DICTID,
                msde.DICTNAME,
                msde.DICTTYPEID as dictTypeId,
                msde.SORTNO     as sortNo,
                msde.STATUS
            from
                mall_sys_dict_entry msde
            where 
                msde.DICTTYPEID = 'ACTION_TYPE'
            order by
                msde.SORTNO desc;        
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> {
        return ourAdditionalHighlighting
    }

    override fun isRainbowType(type: TextAttributesKey?): Boolean {
        return SqlSyntaxHighlighter.STRING.equals(type)
    }

    override fun getLanguage(): Language {
        return SqlLanguage.INSTANCE
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return attributeDescriptors.toTypedArray<AttributesDescriptor>()
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Sql"
    }

}
