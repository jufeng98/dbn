package com.dci.intellij.dbn.debugger.jdbc;

import com.dci.intellij.dbn.common.icon.Icons;
import com.dci.intellij.dbn.common.notification.NotificationGroup;
import com.dci.intellij.dbn.common.thread.Write;
import com.dci.intellij.dbn.common.util.Documents;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.connection.mapping.FileConnectionContextManager;
import com.dci.intellij.dbn.database.common.debug.BreakpointInfo;
import com.dci.intellij.dbn.database.common.debug.BreakpointOperationInfo;
import com.dci.intellij.dbn.database.interfaces.DatabaseDebuggerInterface;
import com.dci.intellij.dbn.debugger.DBDebugConsoleLogger;
import com.dci.intellij.dbn.debugger.DBDebugUtil;
import com.dci.intellij.dbn.debugger.common.breakpoint.DBBreakpointHandler;
import com.dci.intellij.dbn.debugger.common.breakpoint.DBBreakpointType;
import com.dci.intellij.dbn.debugger.common.breakpoint.DBBreakpointUtil;
import com.dci.intellij.dbn.debugger.common.process.DBDebugProcess;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.psql.PSQLFile;
import com.dci.intellij.dbn.object.DBMethod;
import com.dci.intellij.dbn.object.common.DBSchemaObject;
import com.dci.intellij.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.dci.intellij.dbn.vfs.file.DBSourceCodeVirtualFile;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import static com.dci.intellij.dbn.debugger.common.breakpoint.DBBreakpointUtil.*;
import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

public class DBJdbcBreakpointHandler extends DBBreakpointHandler<DBJdbcDebugProcess> {
    protected BreakpointInfo defaultBreakpointInfo;

    DBJdbcBreakpointHandler(XDebugSession session, DBJdbcDebugProcess debugProcess) {
        super(session, debugProcess);
        //resetBreakpoints();
    }

    @Override
    protected void registerDatabaseBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties> breakpoint) {
        DBDebugProcess debugProcess = getDebugProcess();
        DBDebugConsoleLogger console = debugProcess.getConsole();

        XDebugSession session = getSession();

        VirtualFile virtualFile = getVirtualFile(breakpoint);
        Project project = session.getProject();
        if (virtualFile == null) {
            XDebuggerManager debuggerManager = XDebuggerManager.getInstance(project);
            XBreakpointManager breakpointManager = debuggerManager.getBreakpointManager();
            Write.run(project, () -> breakpointManager.removeBreakpoint(breakpoint));
        } else {
            try {
                if (getBreakpointId(breakpoint) != null) {
                    enableBreakpoint(breakpoint);

                } else {
                    BreakpointInfo breakpointInfo = addBreakpoint(breakpoint);
                    String error = breakpointInfo.getError();
                    if (error != null) {
                        handleBreakpointError(breakpoint, error);
                    } else {
                        Integer breakpointId = breakpointInfo.getBreakpointId();
                        setBreakpointId(breakpoint, breakpointId);

                        if (!breakpoint.isEnabled()) {
                            error = disableBreakpoint(breakpointId);
                            if (error != null) {
                                session.updateBreakpointPresentation( breakpoint,
                                        Icons.DEBUG_INVALID_BREAKPOINT,
                                        "INVALID: " + error);
                            }

                        }
                        String breakpointDesc = DBBreakpointUtil.getBreakpointDesc(breakpoint);
                        console.system("Breakpoint added: " + breakpointDesc);
                    }
                }

            } catch (Exception e) {
                conditionallyLog(e);
                handleBreakpointError(breakpoint, e.getMessage());
            }
        }
    }

    @Override
    protected void unregisterDatabaseBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties> breakpoint, boolean temporary) {
        DBDebugProcess debugProcess = getDebugProcess();

        if (!canSetBreakpoints()) return;

        Integer breakpointId = getBreakpointId(breakpoint);
        if (breakpointId != null) {
            DBDebugConsoleLogger console = debugProcess.getConsole();

            VirtualFile virtualFile = getVirtualFile(breakpoint);
            if (virtualFile != null) {
                String breakpointDesc = getBreakpointDesc(breakpoint);
                try {
                    removeBreakpoint(temporary, breakpointId);
                    console.system("Breakpoint removed: " + breakpointDesc);
                } catch (SQLException e) {
                    conditionallyLog(e);
                    console.error("Error removing breakpoint: " + breakpointDesc + ". " + e.getMessage());
                    sendErrorNotification(
                            NotificationGroup.DEBUGGER,
                            "Error unregistering breakpoints: {0}", e);
                } finally {
                    setBreakpointId(breakpoint, null);
                }
            }
        }
    }

    @Override
    public void registerDefaultBreakpoint(DBMethod method) {
        DBEditableObjectVirtualFile mainDatabaseFile = DBDebugUtil.getMainDatabaseFile(method);

        if (mainDatabaseFile == null) return;

        DBSourceCodeVirtualFile sourceCodeFile = (DBSourceCodeVirtualFile) mainDatabaseFile.getMainContentFile();
        PSQLFile psqlFile = (PSQLFile) sourceCodeFile.getPsiFile();
        if (psqlFile == null) return;

        BasePsiElement basePsiElement = psqlFile.lookupObjectDeclaration(method.getObjectType().getGenericType(), method.getName());
        if (basePsiElement == null) return;

        BasePsiElement subject = basePsiElement.findFirstPsiElement(ElementTypeAttribute.SUBJECT);
        int offset = subject.getTextOffset();
        Document document = Documents.getDocument(psqlFile);
        if (document == null) return;

        int line = document.getLineNumber(offset);
        DBSchemaObject schemaObject = DBDebugUtil.getMainDatabaseObject(method);
        if (schemaObject == null) return;

        try {
            defaultBreakpointInfo = getDebuggerInterface().addProgramBreakpoint(
                    method.getSchema().getName(),
                    schemaObject.getName(),
                    schemaObject.getObjectType().getName().toUpperCase(),
                    line,
                    getDebugConnection());
        } catch (SQLException e) {
            conditionallyLog(e);
        }
    }

    @Override
    public void unregisterDefaultBreakpoint() {
        try {
            if (defaultBreakpointInfo != null && defaultBreakpointInfo.getBreakpointId() != null) {
                getDebuggerInterface().removeBreakpoint(defaultBreakpointInfo.getBreakpointId(), getDebugConnection());
            }
        } catch (SQLException e) {
            conditionallyLog(e);
        }
    }

    private DBNConnection getDebugConnection() {
        DBJdbcDebugProcess debugProcess = getDebugProcess();
        return debugProcess.getDebuggerConnection();
    }

    private BreakpointInfo addBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties> breakpoint) throws Exception {
        ConnectionHandler connection = getConnection();
        DatabaseDebuggerInterface debuggerInterface = connection.getDebuggerInterface();
        DBNConnection debugConnection = getDebugConnection();
        DBSchemaObject object = getDatabaseObject(breakpoint);
        return object == null ?
                debuggerInterface.addSourceBreakpoint(
                        breakpoint.getLine(),
                        debugConnection) :
                debuggerInterface.addProgramBreakpoint(
                        object.getSchema().getName(),
                        object.getName(),
                        object.getObjectType().getName().toUpperCase(),
                        breakpoint.getLine(),
                        debugConnection);
    }

    private void removeBreakpoint(boolean temporary, Integer breakpointId) throws SQLException {
        ConnectionHandler connection = getConnection();
        DBNConnection debugConnection = getDebugConnection();
        DatabaseDebuggerInterface debuggerInterface = connection.getDebuggerInterface();
        if (temporary) {
            debuggerInterface.disableBreakpoint(breakpointId, debugConnection);
        } else {
            debuggerInterface.removeBreakpoint(breakpointId, debugConnection);
        }
    }

    private void enableBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties> breakpoint) throws Exception {
        Integer breakpointId = getBreakpointId(breakpoint);
        if (breakpointId != null) {
            ConnectionHandler connection = getConnection();
            DBNConnection debugConnection = getDebugConnection();

            DatabaseDebuggerInterface debuggerInterface = connection.getDebuggerInterface();
            BreakpointOperationInfo breakpointOperationInfo = debuggerInterface.enableBreakpoint(breakpointId, debugConnection);
            String error = breakpointOperationInfo.getError();
            if (error != null) {
                getSession().updateBreakpointPresentation(breakpoint,
                        Icons.DEBUG_INVALID_BREAKPOINT,
                        "INVALID: " + error);
            }

        }
    }

    private String disableBreakpoint(Integer breakpointId) throws SQLException {
        ConnectionHandler connection = getConnection();
        DatabaseDebuggerInterface debuggerInterface = connection.getDebuggerInterface();
        DBNConnection debugConnection = getDebugConnection();
        BreakpointOperationInfo breakpointOperationInfo = debuggerInterface.disableBreakpoint(breakpointId, debugConnection);
        return breakpointOperationInfo.getError();
    }

    private void resetBreakpoints() {
        Project project = getSession().getProject();

        XBreakpointManager breakpointManager = XDebuggerManager.getInstance(project).getBreakpointManager();
        XBreakpoint<?>[] breakpoints = breakpointManager.getAllBreakpoints();

        for (XBreakpoint breakpoint : breakpoints) {
            if (breakpoint.getType() instanceof DBBreakpointType) {
                XLineBreakpoint lineBreakpoint = (XLineBreakpoint) breakpoint;
                VirtualFile virtualFile = getVirtualFile(lineBreakpoint);
                if (virtualFile != null) {
                    FileConnectionContextManager contextManager = FileConnectionContextManager.getInstance(project);
                    ConnectionHandler connection = contextManager.getConnection(virtualFile);

                    if (connection == getDebugProcess().getConnection()) {
                        setBreakpointId(lineBreakpoint, null);
                    }
                }
            }
        }
    }
}
