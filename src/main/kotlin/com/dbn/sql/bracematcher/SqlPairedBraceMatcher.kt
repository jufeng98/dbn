package com.dbn.sql.bracematcher

import com.dbn.sql.psi.SqlTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

/**
 * 提示 IDEA 匹配成对的括号
 *
 * @author yudong
 */
class SqlPairedBraceMatcher : PairedBraceMatcher {
    private val pairs: Array<BracePair> = arrayOf(
        BracePair(SqlTypes.LP, SqlTypes.RP, true),
    )

    override fun getPairs(): Array<BracePair> {
        return pairs
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }
}
