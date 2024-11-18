package com.dbn.sql.inject

import com.dbn.sql.SqlPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl
import kotlin.math.min

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

    override fun createLiteralTextEscaper(): LiteralTextEscaper<PsiLanguageInjectionHost?> {
        return object : LiteralTextEscaper<PsiLanguageInjectionHost?>(this) {
            private lateinit var outSourceOffsets: IntArray

            override fun decode(rangeInsideHost: TextRange, outChars: StringBuilder): Boolean {
                val subText: String = rangeInsideHost.substring(myHost!!.text)
                this.outSourceOffsets = IntArray(subText.length + 1)
                return PsiLiteralExpressionImpl.parseStringCharacters(
                    subText, outChars,
                    this.outSourceOffsets
                )
            }

            override fun getOffsetInHost(offsetInDecoded: Int, rangeInsideHost: TextRange): Int {
                val result =
                    if (offsetInDecoded < this.outSourceOffsets.size) this.outSourceOffsets.get(offsetInDecoded) else -1
                return if (result == -1) -1
                else min(
                    result,
                    rangeInsideHost.length
                ) + rangeInsideHost.startOffset
            }

            override fun isOneLine(): Boolean {
                return true
            }
        }
    }
}
