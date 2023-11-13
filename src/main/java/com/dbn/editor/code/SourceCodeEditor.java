package com.dbn.editor.code;

import com.dbn.common.editor.BasicTextEditorImpl;
import com.dbn.editor.code.content.SourceCodeOffsets;
import com.dbn.editor.DBContentType;
import com.dbn.editor.EditorProviderId;
import com.dbn.language.common.psi.BasePsiElement;
import com.dbn.language.common.psi.PsiUtil;
import com.dbn.language.psql.PSQLFile;
import com.dbn.object.common.DBObject;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class SourceCodeEditor extends BasicTextEditorImpl<DBSourceCodeVirtualFile>{
    private final DBObjectRef<DBSchemaObject> object;

    public SourceCodeEditor(Project project, final DBSourceCodeVirtualFile sourceCodeFile, String name, EditorProviderId editorProviderId) {
        super(project, sourceCodeFile, name, editorProviderId);
        object = DBObjectRef.of(sourceCodeFile.getObject());
    }

    public DBSchemaObject getObject() {
        return DBObjectRef.get(object);
    }

    public int getHeaderEndOffset() {
        SourceCodeOffsets offsets = getVirtualFile().getOffsets();
        return offsets.getHeaderEndOffset();
    }

    public void navigateTo(DBObject object) {
        PsiFile file = PsiUtil.getPsiFile(getObject().getProject(), getVirtualFile());
        if (file instanceof PSQLFile) {
            PSQLFile psqlFile = (PSQLFile) file;
            BasePsiElement navigable = psqlFile.lookupObjectDeclaration(object.getObjectType(), object.getName());
            if (navigable == null) navigable = psqlFile.lookupObjectSpecification(object.getObjectType(), object.getName());
            if (navigable != null) navigable.navigate(true);
        }
    }

    public DBContentType getContentType() {
        return getVirtualFile().getContentType();
    }
}
