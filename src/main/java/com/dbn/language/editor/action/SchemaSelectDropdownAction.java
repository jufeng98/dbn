package com.dbn.language.editor.action;

import com.dbn.common.icon.Icons;
import com.dbn.common.action.Lookups;
import com.dbn.common.dispose.Checks;
import com.dbn.common.ui.misc.DBNComboBoxAction;
import com.dbn.connection.ConnectionHandler;
import com.dbn.connection.SchemaId;
import com.dbn.connection.mapping.FileConnectionContextManager;
import com.dbn.ddl.DDLFileAttachmentManager;
import com.dbn.object.DBSchema;
import com.dbn.object.lookup.DBObjectRef;
import com.dbn.vfs.DatabaseFileSystem;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SchemaSelectDropdownAction extends DBNComboBoxAction implements DumbAware {
    private static final String NAME = "Schema";

    @Override
    @NotNull
    protected DefaultActionGroup createPopupActionGroup(JComponent component) {
        Project project = Lookups.getProject(component);
        VirtualFile virtualFile = Lookups.getVirtualFile(component);
        return createActionGroup(project, virtualFile);
    }

    @Override
    protected @NotNull DefaultActionGroup createPopupActionGroup(@NotNull JComponent button, @NotNull DataContext dataContext) {
        Project project = Lookups.getProject(button);
        VirtualFile virtualFile = Lookups.getVirtualFile(dataContext);
        return createActionGroup(project, virtualFile);
    }

    private static DefaultActionGroup createActionGroup(Project project, VirtualFile virtualFile) {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        if (virtualFile != null) {
            FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
            ConnectionHandler activeConnection = contextManager.getConnection(virtualFile);
            if (Checks.isValid(activeConnection) && !activeConnection.isVirtual()) {
                for (DBSchema schema : activeConnection.getObjectBundle().getSchemas()){
                    actionGroup.add(new SchemaSelectAction(schema));
                }
            }
        }
        return actionGroup;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = Lookups.getProject(e);
        VirtualFile virtualFile = Lookups.getVirtualFile(e);
        String text = NAME;

        Icon icon = null;
        boolean visible = false;
        boolean enabled = true;

        if (project != null && virtualFile != null) {
            FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
            ConnectionHandler activeConnection = contextManager.getConnection(virtualFile);
            visible = activeConnection != null && !activeConnection.isVirtual();
            if (visible) {
                SchemaId schema = contextManager.getDatabaseSchema(virtualFile);
                if (schema != null) {
                    text = schema.getName();
                    icon = Icons.DBO_SCHEMA;
                    enabled = true;
                }

                if (virtualFile.isInLocalFileSystem()) {
                    DDLFileAttachmentManager fileAttachmentManager = DDLFileAttachmentManager.getInstance(project);
                    DBObjectRef editableObject = fileAttachmentManager.getMappedObjectRef(virtualFile);
                    if (editableObject != null) {
                        boolean isOpened = DatabaseFileSystem.isFileOpened(editableObject);
                        if (isOpened) {
                            enabled = false;
                        }
                    }
                }
            }
        }

        Presentation presentation = e.getPresentation();
        presentation.setText(text, false);
        presentation.setDescription("Select current schema");
        presentation.setIcon(icon);
        presentation.setVisible(visible);
        presentation.setEnabled(enabled);
    }
 }
