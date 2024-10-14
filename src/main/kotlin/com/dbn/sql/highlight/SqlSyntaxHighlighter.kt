package com.dbn.sql.highlight

import com.dbn.code.sql.color.SQLTextAttributesKeys
import com.dbn.sql.parser.SqlAdapter
import com.dbn.sql.psi.SqlTypes
import com.dbn.utils.SqlUtils
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

/**
 * @author yudong
 */
class SqlSyntaxHighlighter : SyntaxHighlighterBase() {
    private val ourAttributes: MutableMap<IElementType, TextAttributesKey> = HashMap()

    init {
        fillMap(ourAttributes, IDENTIFIER, SqlTypes.ID)
        fillMap(ourAttributes, DOT, SqlTypes.DOT)
        fillMap(ourAttributes, NUMBER, SqlTypes.DIGIT)
        fillMap(ourAttributes, STRING, SqlTypes.STRING)
        fillMap(ourAttributes, SQLTextAttributesKeys.PARAMETER, SqlTypes.MYBATIS_OGNL)
        fillMap(ourAttributes, COMMENT, SqlTypes.COMMENT, SqlTypes.BLOCK_COMMENT)
        fillMap(ourAttributes, BAD_CHARACTER, TokenType.BAD_CHARACTER)

        for (sqlKeyword in SqlUtils.SQL_KEYWORDS) {
            ourAttributes[sqlKeyword] = KEYWORD
        }
    }

    override fun getHighlightingLexer(): Lexer {
        return SqlAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(ourAttributes[tokenType])
    }

    companion object {
        val IDENTIFIER: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("SQL_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val KEYWORD: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("SQL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val STRING: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("SQL_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("SQL_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val DOT: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("SQL_DOT", DefaultLanguageHighlighterColors.DOT)
        val COMMENT: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("SQL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BAD_CHARACTER: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey("SQL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    }
}