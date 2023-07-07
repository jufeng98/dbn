package com.dci.intellij.dbn.debugger.jdwp.process;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.debugger.common.config.DBRunConfig;
import com.dci.intellij.dbn.debugger.jdwp.config.DBJdwpRunConfig;
import com.intellij.debugger.DebugEnvironment;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.DefaultDebugEnvironment;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RemoteConnection;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.util.Range;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import org.jetbrains.annotations.NotNull;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;


public abstract class DBJdwpLocalProcessStarter extends DBJdwpProcessStarter {
    DBJdwpLocalProcessStarter(ConnectionHandler connection) {
        super(connection);
    }

    private static int findFreePort(int minPortNumber, int maxPortNumber) throws ExecutionException {
        for (int portNumber = minPortNumber; portNumber < maxPortNumber; portNumber++) {
            try {
                try (ServerSocket ignored = new ServerSocket(portNumber)) {
                    return portNumber;
                }
            } catch (Exception ignore) {
            }
        }
        throw new ExecutionException("Could not find free port in the range " + minPortNumber + " - " + maxPortNumber);
    }

    @NotNull
    @Override
    public final XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
        Executor executor = DefaultDebugExecutor.getDebugExecutorInstance();
        RunProfile runProfile = session.getRunProfile();
        assertNotNull(runProfile, "Invalid run profile");
        if (runProfile instanceof DBRunConfig) {
            DBRunConfig runConfig = (DBRunConfig) runProfile;
            runConfig.setCanRun(true);
        }

        ExecutionEnvironment environment = ExecutionEnvironmentBuilder.create(session.getProject(), executor, runProfile).build();
        DBJdwpRunConfig jdwpRunConfig = (DBJdwpRunConfig) runProfile;
        Range<Integer> portRange = jdwpRunConfig.getTcpPortRange();
        int freePort = findFreePort(portRange.getFrom(), portRange.getTo());
        String debugHostName = null;
        try {
            debugHostName = Inet4Address.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            // just leave it null;
        }
        InetAddress hostAddress = jdwpRunConfig.getDebuggerHostIPAddr();
        if (hostAddress != null) {
            if (hostAddress.getHostAddress() != null) {
                debugHostName = hostAddress.getHostAddress();
            }
        }
        RemoteConnection remoteConnection = new RemoteConnection(true, debugHostName, Integer.toString(freePort), true);

        RunProfileState state = Failsafe.nn(runProfile.getState(executor, environment));

        DebugEnvironment debugEnvironment = new DefaultDebugEnvironment(environment, state, remoteConnection, true);
        DebuggerManagerEx debuggerManagerEx = DebuggerManagerEx.getInstanceEx(session.getProject());
        DebuggerSession debuggerSession = debuggerManagerEx.attachVirtualMachine(debugEnvironment);
        assertNotNull(debuggerSession, "Could not initialize JDWP listener");

        return createDebugProcess(session, debuggerSession, debugHostName, freePort);

    }

}
