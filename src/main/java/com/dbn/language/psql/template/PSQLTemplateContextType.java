package com.dbn.language.psql.template;

import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.LeafPsiElement;
import com.dbn.language.common.psi.PsiUtil;
import com.dbn.language.psql.PSQLLanguage;
import com.dbn.language.sql.SQLLanguage;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PSQLTemplateContextType extends TemplateContextType {
    protected PSQLTemplateContextType() {
        super("PL/SQL", "PL/SQL (DBN)");
    }

    @Override
    public boolean isInContext(@NotNull PsiFile file, int offset) {
        Language language = file.getLanguage();
        if (language instanceof SQLLanguage) {
            // support PSQL in SQL language
            LeafPsiElement leafPsiElement = PsiUtil.lookupLeafBeforeOffset(file, offset);
            if (leafPsiElement != null) {
                if (leafPsiElement.getLanguage() instanceof PSQLLanguage) {
                    BasePsiElement scopePsiElement = leafPsiElement.getEnclosingScopeElement();
                    return scopePsiElement != null && scopePsiElement.getTextRange().contains(offset);
                }
            }
        }
        return language instanceof PSQLLanguage;
    }

    @Nullable
    @Override
    public SyntaxHighlighter createHighlighter() {
        return PSQLLanguage.INSTANCE.getMainLanguageDialect().getSyntaxHighlighter();
    }
}
