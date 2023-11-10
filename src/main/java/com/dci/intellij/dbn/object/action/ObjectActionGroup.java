package com.dci.intellij.dbn.object.action;

import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.database.DatabaseFeature;
import com.dci.intellij.dbn.editor.DBContentType;
import com.dci.intellij.dbn.execution.compiler.action.CompileActionGroup;
import com.dci.intellij.dbn.execution.method.action.MethodDebugAction;
import com.dci.intellij.dbn.execution.method.action.MethodRunAction;
import com.dci.intellij.dbn.execution.method.action.ProgramMethodDebugAction;
import com.dci.intellij.dbn.execution.method.action.ProgramMethodRunAction;
import com.dci.intellij.dbn.generator.action.GenerateStatementActionGroup;
import com.dci.intellij.dbn.object.*;
import com.dci.intellij.dbn.object.common.DBObject;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.object.common.list.DBObjectNavigationList;
import com.dci.intellij.dbn.object.common.list.action.HideAuditColumnsToggleAction;
import com.dci.intellij.dbn.object.common.list.action.HideEmptySchemasToggleAction;
import com.dci.intellij.dbn.object.common.list.action.HidePseudoColumnsToggleAction;
import com.dci.intellij.dbn.object.dependency.action.ObjectDependencyTreeAction;
import com.dci.intellij.dbn.object.type.DBObjectType;
import com.dci.intellij.dbn.vfs.DBConsoleType;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAware;

import java.util.List;

import static com.dci.intellij.dbn.object.common.property.DBObjectProperty.*;

public class ObjectActionGroup extends DefaultActionGroup implements DumbAware {

    public ObjectActionGroup(DBObject object) {
        if(object instanceof DBSchemaObject) {
            DBSchemaObject schemaObject = (DBSchemaObject) object;

            if (object.is(EDITABLE)) {
                DBContentType contentType = schemaObject.getContentType();
                if (contentType == DBContentType.DATA || contentType == DBContentType.CODE_AND_DATA) {
                    add(new ObjectEditDataAction(schemaObject));
                } 

                if (contentType == DBContentType.CODE || contentType == DBContentType.CODE_AND_DATA || contentType == DBContentType.CODE_SPEC_AND_BODY) {
                    if (DatabaseFeature.OBJECT_SOURCE_EDITING.isSupported(object)) {
                        add(new ObjectEditCodeAction(schemaObject));
                    }
                }
            }

            if (object.is(COMPILABLE) && DatabaseFeature.OBJECT_INVALIDATION.isSupported(object)) {
                add(new CompileActionGroup(schemaObject));
            }

            if (object.is(DISABLEABLE) && DatabaseFeature.OBJECT_DISABLING.isSupported(object)) {
                add(new ObjectEnableDisableAction(schemaObject));
            }

            if (object.is(SCHEMA_OBJECT)) {
                if (object.getObjectType() != DBObjectType.CONSTRAINT || DatabaseFeature.CONSTRAINT_MANIPULATION.isSupported(object)) {
                    add(new ObjectDropAction((DBSchemaObject) object));
                }

                //add(new TestAction(object));
            }
        }

        if (object instanceof DBMethod ) {
            addSeparator();
            add(new MethodRunAction((DBMethod) object));
            if (DatabaseFeature.DEBUGGING.isSupported(object)) {
                add(new MethodDebugAction((DBMethod) object));
            }
        }

        if (object instanceof DBProgram && object.is(SCHEMA_OBJECT)) {
            addSeparator();
            add(new ProgramMethodRunAction((DBProgram) object));
            if (DatabaseFeature.DEBUGGING.isSupported(object)) {
                add(new ProgramMethodDebugAction((DBProgram) object));
            }
        }

        if(object instanceof DBSchemaObject) {
            if(object.is(REFERENCEABLE) && DatabaseFeature.OBJECT_DEPENDENCIES.isSupported(object)) {
                addSeparator();
                add (new ObjectDependencyTreeAction((DBSchemaObject) object));
            }
        }

        List<DBObjectNavigationList> navigationLists = object.getNavigationLists();
        if (navigationLists != null && !navigationLists.isEmpty()) {
            if (object.isNot(REFERENCEABLE)) addSeparator();
            //add(new DbsGoToActionGroup(linkLists));
            for (DBObjectNavigationList<?> navigationList : navigationLists) {
                DBObject parentObject = object.getParentObject();
                if (navigationList.isLazy()) {
                    add(new ObjectLazyNavigationListAction(parentObject, navigationList));
                } else {
                    add(new ObjectNavigationListActionGroup(parentObject, navigationList, false));
                }
            }
        }
        ConnectionHandler connection = object.getConnection();
        if (object instanceof DBConsole) {
            DBConsole console = (DBConsole) object;
            add(new ConsoleRenameAction(console));
            add(new ConsoleDeleteAction(console));
            addSeparator();
            add(new ConsoleCreateAction(connection, DBConsoleType.STANDARD));
            if (DatabaseFeature.DEBUGGING.isSupported(connection)) {
                add(new ConsoleCreateAction(connection, DBConsoleType.DEBUG));
            }
        }
        
        if (getChildrenCount() > 0){
            addSeparator();
        }
        addActionGroup(new GenerateStatementActionGroup(object));

        addSeparator();
        if (object instanceof DBColumn) {
            add(new HideAuditColumnsToggleAction(connection));
            add(new HidePseudoColumnsToggleAction(connection));
        } else if (object instanceof DBSchema) {
            add(new HideEmptySchemasToggleAction(connection));
        }
        add(new RefreshActionGroup(object));

        //add(new ObjectPropertiesAction(object));
        //add(new TestAction(object));
    }

    private void addActionGroup(DefaultActionGroup actionGroup) {
        if (actionGroup.getChildrenCount() > 0) {
            add(actionGroup);
        }

    }



}
