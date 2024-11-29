package com.dbn.debugger.common.breakpoint;

import com.dbn.common.listener.DBNFileEditorManagerListener;
import com.dbn.vfs.file.DBEditableObjectVirtualFile;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

import static com.dbn.common.dispose.Failsafe.guarded;

/**
 * WORKAROUND: Breakpoints do not seem to be registered properly in the XLineBreakpointManager.
 * This way the breakpoints get updated as soon as the file is opened.
 */
public class DBBreakpointUpdaterFileEditorListener extends DBNFileEditorManagerListener {
    @Override
    public void whenFileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (file instanceof DBEditableObjectVirtualFile databaseFile) {
            guarded(() -> registerBreakpoints(source, databaseFile));
        }
    }

    private static void registerBreakpoints(@NotNull FileEditorManager source, DBEditableObjectVirtualFile databaseFile) {
        XDebuggerManager debuggerManager = XDebuggerManager.getInstance(source.getProject());
        XBreakpointManager breakpointManager = debuggerManager.getBreakpointManager();
        for (XBreakpoint<?> breakpoint : breakpointManager.getAllBreakpoints()) {
            if (breakpoint instanceof XLineBreakpoint<?> lineBreakpoint) {
                DBBreakpointUtil.setBreakpointId(lineBreakpoint, null);
                VirtualFile virtualFile = DBBreakpointUtil.getVirtualFile(lineBreakpoint);
                if (databaseFile.equals(virtualFile)) {

                    try {
                        Method method = breakpointManager.getClass().getDeclaredMethod("getLineBreakpointManager");
                        method.setAccessible(true);
                        Object lineBreakpointManager = method.invoke(breakpointManager);

                        Class<?> aClass = Class.forName("com.intellij.xdebugger.impl.breakpoints.XLineBreakpointImpl");
                        method = lineBreakpointManager.getClass().getDeclaredMethod("registerBreakpoint", aClass, Boolean.class);
                        method.setAccessible(true);
                        //noinspection JavaReflectionInvocation
                        method.invoke(lineBreakpointManager, lineBreakpoint, true);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
