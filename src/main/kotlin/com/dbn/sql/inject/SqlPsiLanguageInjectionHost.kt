package com.dbn.sql.inject

import com.dbn.sql.SqlPsiElement
import com.intellij.json.psi.impl.JSStringLiteralEscaper
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.impl.source.tree.LeafElement

/**
 * 为元素添加注入功能支持
 *
 * @author yudong
 */
open class SqlPsiLanguageInjectionHost(node: ASTNode) : SqlPsiElement(node), PsiLanguageInjectionHost {
    override fun isValidHost(): Boolean {
        return true
    }

    override fun updateText(text: String): PsiLanguageInjectionHost {
        val valueNode = node.firstChildNode
        val leafElement = valueNode is LeafElement

        assert(leafElement)

        (valueNode as LeafElement).replaceWithText(text)

        return this
    }

    override fun createLiteralTextEscaper(): JSStringLiteralEscaper<PsiLanguageInjectionHost?> {
        return object : JSStringLiteralEscaper<PsiLanguageInjectionHost?>(this) {
            override fun isRegExpLiteral(): Boolean {
                return false
            }

            override fun isOneLine(): Boolean {
                return true
            }
        }
    }
}
