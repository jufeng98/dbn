package com.dbn.language.sql;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

public class SqlElementFactory {
    public static PsiElement createSqlElement(Project project, String sql) {
        final SQLFile file = createFile(project, sql);
        return file.getFirstChild();
    }

    public static SQLFile createFile(Project project, String text) {
        String name = "dummy.sql";
        return (SQLFile) PsiFileFactory.getInstance(project).createFileFromText(name, SQLLanguage.INSTANCE, text);
    }
}
