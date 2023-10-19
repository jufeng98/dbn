package com.dci.intellij.dbn.debugger.jdwp.process;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.jdbc.DBNConnection;
import com.dci.intellij.dbn.debugger.common.config.DBRunConfig;
import com.dci.intellij.dbn.debugger.common.process.DBDebugProcessStarter;
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
import com.intellij.openapi.util.Key;
import com.intellij.util.Range;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import lombok.extern.slf4j.Slf4j;
import com.jetbrains.jdi.GenericAttachingConnector;
import com.jetbrains.jdi.SocketTransportService;
import com.jetbrains.jdi.VirtualMachineManagerImpl;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.spi.Connection;
//import oracle.jdbc.OracleConnection;
//import oracle.jdbc.datasource.impl.OracleDataSource;
//import oracle.net.ns.NSTunnelConnection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
/**
 * This is the parent of all JDWP process starter classes for debugging the Oracle the database from
 * DBN. Sub-classes should implement the start method, which will be called by the framework to
 * launch the virtual debug process object and initialize PL/SQL debugging.
 */
public abstract class DBJdwpProcessStarter extends DBDebugProcessStarter {

    public static final Key<Integer> JDWP_DEBUGGER_PORT = new Key<>("JDWP_DEBUGGER_PORT");



    DBJdwpProcessStarter(ConnectionHandler connection) {
        super(connection);
    }

    /**
     *there is two implementations of this method , the first one to debug local database ,
     *  second one is for debugging database in cloud
     * @param session session to be passed to {@link XDebugProcess#XDebugProcess} constructor
     * @return
     * @throws ExecutionException
     */
    abstract public  XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException ;

    protected abstract DBJdwpDebugProcess createDebugProcess(@NotNull XDebugSession session, DebuggerSession debuggerSession, String hostname, int tcpPort);

    @NotNull
    protected <T> T assertNotNull(@Nullable T object, String message) throws ExecutionException {
        if (object == null) {
            throw new ExecutionException(message);
        }
        return object;
    }
}
