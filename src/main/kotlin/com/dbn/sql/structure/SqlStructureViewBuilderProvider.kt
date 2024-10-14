package com.dbn.sql.structure

import com.dbn.sql.parser.SqlFile
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

/**
 * @author yudong
 */
class SqlStructureViewBuilderProvider : PsiStructureViewFactory {

    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        if (psiFile !is SqlFile) {
            return null
        }

        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return SqlStructureViewModel(psiFile, editor)
            }

            override fun isRootNodeShown(): Boolean {
                return false
            }
        }
    }

}
