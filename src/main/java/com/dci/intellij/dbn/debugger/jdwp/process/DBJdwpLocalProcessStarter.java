package com.dci.intellij.dbn.debugger.jdwp.process;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.util.Strings;
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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
public abstract class DBJdwpLocalProcessStarter extends DBJdwpProcessStarter {
    DBJdwpLocalProcessStarter(ConnectionHandler connection) {
        super(connection);
    }

    private static int findFreePort(String host, int minPortNumber, int maxPortNumber) throws ExecutionException {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new ExecutionException("Failed to resolve host '" + host + "'", e);
        }

        for (int portNumber = minPortNumber; portNumber < maxPortNumber; portNumber++) {
            try (ServerSocket ignored = new ServerSocket(portNumber, 50, inetAddress)) {
                return portNumber;
            } catch (Exception e) {
                conditionallyLog(e);
            }
        }
        throw new ExecutionException("Could not find any free port on host '" + host + "' in the range " + minPortNumber + " - " + maxPortNumber);
    }

    /**
     * local database start's implementation: set up the ip host and port in  intellij
     * debugger framework , also setting up that we want to use listen connector and make the
     * debugger listen till the database connect using sockets
     * @param session session to be passed to {@link XDebugProcess#XDebugProcess} constructor
     * @return
     * @throws ExecutionException
     */
    @NotNull
    @Override
    public final XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
        Executor executor = DefaultDebugExecutor.getDebugExecutorInstance();
        RunProfile runProfile = session.getRunProfile();
        assertNotNull(runProfile, "Invalid run profile");


        ExecutionEnvironment environment = ExecutionEnvironmentBuilder.create(session.getProject(), executor, runProfile).build();
        DBJdwpRunConfig jdwpRunConfig = (DBJdwpRunConfig) runProfile;
        Range<Integer> portRange = jdwpRunConfig.getTcpPortRange();
        String tcpHost = resolveTcpHost(jdwpRunConfig);
        int freePort = findFreePort(tcpHost,portRange.getFrom(), portRange.getTo());

        RemoteConnection remoteConnection = new RemoteConnection(true, tcpHost, Integer.toString(freePort), true);

        RunProfileState state = Failsafe.nn(runProfile.getState(executor, environment));

        DebugEnvironment debugEnvironment = new DefaultDebugEnvironment(environment, state, remoteConnection, true);
        DebuggerManagerEx debuggerManagerEx = DebuggerManagerEx.getInstanceEx(session.getProject());
        DebuggerSession debuggerSession = debuggerManagerEx.attachVirtualMachine(debugEnvironment);
        assertNotNull(debuggerSession, "Could not initialize JDWP listener");

        return createDebugProcess(session, debuggerSession, tcpHost, freePort);

    }

    private static String resolveTcpHost(DBJdwpRunConfig jdwpRunConfig) {
        String tcpHost = jdwpRunConfig.getTcpHostAddress();
        try {
            tcpHost = Strings.isEmptyOrSpaces(tcpHost) ?
                    Inet4Address.getLocalHost().getHostAddress() :
                    InetAddress.getAllByName(tcpHost)[0].getHostAddress();

        } catch (UnknownHostException e) {
            conditionallyLog(e);
            // TODO log to the debugger console instead
            log.warn("Failed to resolve TCP host address '{}'. Using 'localhost'", tcpHost, e);
            tcpHost =  "localhost";

        }
        return tcpHost;
    }


}
