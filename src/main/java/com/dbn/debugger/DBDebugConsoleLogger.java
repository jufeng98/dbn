package com.dbn.debugger;

import com.dbn.common.locale.Formatter;
import com.dbn.common.message.MessageType;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.util.Key;
import com.intellij.xdebugger.XDebugSession;

import java.util.Date;

import static com.dbn.diagnostics.Diagnostics.conditionallyLog;

public class DBDebugConsoleLogger {
    protected XDebugSession session;

    public DBDebugConsoleLogger(XDebugSession session) {
        this.session = session;
    }

    public void system(String text) {
        log(text, MessageType.SYSTEM);
    }

    public void error(String text) {
        log(text, MessageType.ERROR);
    }

    public void info(String text) {
        log(text, MessageType.INFO);
    }

    public void warning(String text) {
        log(text, MessageType.WARNING);
    }


    private void log(String text, MessageType messageType) {
        try {
            RunContentDescriptor descriptor = session.getRunContentDescriptor();
            ProcessHandler processHandler = descriptor.getProcessHandler();
            if (processHandler == null) return;

            if (!processHandler.isStartNotified()) processHandler.startNotify();

            Formatter formatter = Formatter.getInstance(session.getProject());
            String date = formatter.formatDateTime(new Date());
            String prefix =
                    messageType == MessageType.ERROR ? "ERROR: " :
                    messageType == MessageType.WARNING ? "WARNING: " : "INFO: ";

            text = prefix + date + ": " + text + "\n";
            Key outputType =
                    messageType == MessageType.SYSTEM ? ProcessOutputTypes.SYSTEM :
                    messageType == MessageType.ERROR  ? ProcessOutputTypes.STDERR : ProcessOutputTypes.STDOUT;
            processHandler.notifyTextAvailable(text, outputType);

        } catch (IllegalStateException e) {
            conditionallyLog(e);
        }

    }
}
