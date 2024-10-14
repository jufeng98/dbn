package com.dbn.sql

import com.dbn.sql.psi.SqlTypes
import com.intellij.lang.ASTFactory
import com.intellij.lang.DefaultASTFactory
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType

/**
 * @author yudong
 */
class SqlASTFactory : ASTFactory() {
    private val myDefaultASTFactory: DefaultASTFactory = ApplicationManager.getApplication().getService(
        DefaultASTFactory::class.java
    )

    override fun createLeaf(type: IElementType, text: CharSequence): LeafElement {
        if (type == SqlTypes.COMMENT || type == SqlTypes.BLOCK_COMMENT) {
            return myDefaultASTFactory.createComment(type, text)
        }

        return LeafPsiElement(type, text)
    }
}
