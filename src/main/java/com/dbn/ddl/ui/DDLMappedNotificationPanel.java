package com.dbn.ddl.ui;

import com.dbn.common.editor.EditorNotificationPanel;
import com.dbn.common.message.MessageType;
import com.dbn.ddl.DDLFileAttachmentManager;
import com.dbn.editor.DatabaseFileEditorManager;
import com.dbn.object.common.DBSchemaObject;
import com.dbn.object.lookup.DBObjectRef;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import static com.dbn.common.dispose.Checks.isNotValid;

public class DDLMappedNotificationPanel extends EditorNotificationPanel {

    public DDLMappedNotificationPanel(@NotNull final VirtualFile virtualFile, final DBSchemaObject editableObject) {
        super(MessageType.NEUTRAL);
        Project project = editableObject.getProject();
        DBObjectRef<DBSchemaObject> editableObjectRef = DBObjectRef.of(editableObject);
        String objectName = editableObject.getQualifiedNameWithType();
        String objectTypeName = editableObject.getObjectType().getName();
        setText("This DDL file is attached to the database " + objectName + ". " +
                "Changes done to the " + objectTypeName + " are mirrored to this DDL file, overwriting any changes you may do to it.");
        createActionLabel("Detach", () -> {
            if (isNotValid(project)) return;

            DDLFileAttachmentManager attachmentManager = DDLFileAttachmentManager.getInstance(project);
            attachmentManager.detachDDLFile(virtualFile);
            DBSchemaObject object = DBObjectRef.get(editableObjectRef);
            if (object == null) return;

            DatabaseFileEditorManager editorManager = DatabaseFileEditorManager.getInstance(project);
            editorManager.reopenEditor(object);
        });
    }
}
