package com.dbn.debugger.common.breakpoint;

import com.dbn.common.icon.Icons;
import com.dbn.common.notification.NotificationSupport;
import com.dbn.connection.ConnectionHandler;
import com.dbn.database.interfaces.DatabaseDebuggerInterface;
import com.dbn.debugger.DBDebugConsoleLogger;
import com.dbn.debugger.common.process.DBDebugProcess;
import com.dbn.object.DBMethod;
import com.dbn.object.common.DBObject;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.breakpoints.XBreakpointProperties;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dbn.debugger.common.breakpoint.DBBreakpointUtil.getBreakpointDesc;
import static com.dbn.debugger.common.process.DBDebugProcessStatus.BREAKPOINT_SETTING_ALLOWED;

@Getter
public abstract class DBBreakpointHandler<T extends DBDebugProcess>
        extends XBreakpointHandler<XLineBreakpoint<XBreakpointProperties<?>>> implements NotificationSupport {
    private final XDebugSession session;
    private final T debugProcess;

    protected DBBreakpointHandler(XDebugSession session, T debugProcess) {
        super(DBBreakpointType.class);
        this.session = session;
        this.debugProcess = debugProcess;
    }

    @Override
    public Project getProject() {
        return session.getProject();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean canSetBreakpoints() {
        return getDebugProcess().is(BREAKPOINT_SETTING_ALLOWED);
    }

    @Override
    public final void registerBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties<?>> breakpoint) {
        if (!canSetBreakpoints()) return;

        XBreakpointProperties<?> properties = breakpoint.getProperties();
        if (properties instanceof DBBreakpointProperties breakpointProperties) {
            if (getConnection() == breakpointProperties.getConnection()) {
                registerDatabaseBreakpoint(breakpoint);
            }
        }
    }

    @Override
    public final void unregisterBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties<?>> breakpoint, boolean temporary) {
        XBreakpointProperties<?> properties = breakpoint.getProperties();
        if (properties instanceof DBBreakpointProperties breakpointProperties) {
            if (getConnection() == breakpointProperties.getConnection()) {
                unregisterDatabaseBreakpoint(breakpoint, temporary);
            }
        }
    }

    protected abstract void registerDatabaseBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties<?>> breakpoint);

    protected abstract void unregisterDatabaseBreakpoint(@NotNull XLineBreakpoint<XBreakpointProperties<?>> breakpoint, boolean temporary);

    public void registerBreakpoints(@NotNull List<XLineBreakpoint<XBreakpointProperties<?>>> breakpoints, List<? extends DBObject> objects) {
        for (XLineBreakpoint<XBreakpointProperties<?>> breakpoint : breakpoints) {
            registerBreakpoint(breakpoint);
        }
    }

    protected void handleBreakpointError(@NotNull XLineBreakpoint<XBreakpointProperties<?>> breakpoint, String error) {
        DBDebugConsoleLogger console = getConsole();
        XDebugSession session = getSession();
        String breakpointDesc = getBreakpointDesc(breakpoint);
        console.error("Failed to add breakpoint: " + breakpointDesc + " (" + error + ")");
        session.updateBreakpointPresentation(breakpoint,
                Icons.DEBUG_INVALID_BREAKPOINT,
                "INVALID: " + error);
    }

    private DBDebugConsoleLogger getConsole() {
        return getDebugProcess().getConsole();
    }

    protected ConnectionHandler getConnection() {
        return getDebugProcess().getConnection();
    }

    protected DatabaseDebuggerInterface getDebuggerInterface() {
        return getConnection().getDebuggerInterface();
    }

    public abstract void registerDefaultBreakpoint(DBMethod method);

    public abstract void unregisterDefaultBreakpoint();
}
