package com.dbn.sql.intent

import com.dbn.utils.TooltipUtils
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.codeInspection.util.IntentionName
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.IncorrectOperationException

/**
 * @author yudong
 */
class GenerateXmlResultAction : BaseIntentionAction() {
    override fun getText(): @IntentionName String {
        @Suppress("DialogTitleCapitalization")
        return "生成 MyBatis result 元素"
    }

    override fun getFamilyName(): @IntentionFamilyName String {
        return text
    }


    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile): Boolean {
        if (psiFile !is XmlFile) {
            return false
        }

        val element = getElement(editor, psiFile)

        val xmlTag = PsiTreeUtil.getParentOfType(element, XmlTag::class.java) ?: return false
        val tagName = xmlTag.name
        if (tagName != "resultMap" && tagName != "collection" && tagName != "association") {
            return false
        }

        val type = tryGetType(xmlTag)

        return type != null
    }

    private fun tryGetType(xmlTag: XmlTag): String? {
        var type = xmlTag.getAttributeValue("type")
        if (!type.isNullOrBlank()) {
            return type
        }

        type = xmlTag.getAttributeValue("ofType")
        if (!type.isNullOrBlank()) {
            return type
        }

        type = xmlTag.getAttributeValue("javaType")
        if (!type.isNullOrBlank()) {
            return type
        }

        return null
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val element = getElement(editor, psiFile)!!
        val xmlTag = PsiTreeUtil.getParentOfType(element, XmlTag::class.java)!!

        val type = tryGetType(xmlTag) ?: return

        val module = ModuleUtil.findModuleForFile(psiFile) ?: return
        val scope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)

        val javaPsiFacade = JavaPsiFacade.getInstance(project)
        val psiClass = javaPsiFacade.findClass(type, scope)
        if (psiClass == null) {
            TooltipUtils.showTooltip("无法解析$type!", project)
            return
        }

        val resultStr = psiClass.allFields.joinToString("\n") {
            val name = it.name
            if (name == "id" || name.contains("Id")) {
                "<id property=\"$name\" column=\"$name\"/>"
            } else {
                "<result property=\"$name\" column=\"$name\"/>"
            }
        }

        val caretModel = editor.caretModel
        val position = caretModel.offset
        val document = PsiDocumentManager.getInstance(project).getDocument(psiFile)!!
        document.insertString(position, resultStr)
    }

    private fun getElement(editor: Editor, file: PsiFile): PsiElement? {
        val caretModel = editor.caretModel
        val position = caretModel.offset
        return file.findElementAt(position)
    }
}
