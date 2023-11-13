package com.dbn.debugger.jdwp.process;

import com.dbn.connection.ConnectionHandler;
import com.dbn.debugger.common.process.DBDebugProcessStarter;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.execution.ExecutionException;
import com.intellij.openapi.util.Key;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is the parent of all JDWP process starter classes for debugging the Oracle the database from
 * DBN. Sub-classes should implement the start method, which will be called by the framework to
 * launch the virtual debug process object and initialize PL/SQL debugging.
 */
@Slf4j
public abstract class DBJdwpProcessStarter extends DBDebugProcessStarter {

    public static final Key<Integer> JDWP_DEBUGGER_PORT = new Key<>("JDWP_DEBUGGER_PORT");



    DBJdwpProcessStarter(ConnectionHandler connection) {
        super(connection);
    }

    /**
     *there is two implementations of this method , the first one to debug local database ,
     *  second one is for debugging database in cloud
     * @param session session to be passed to {@link XDebugProcess#XDebugProcess} constructor
     */
    abstract public XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException ;

    protected abstract DBJdwpDebugProcess createDebugProcess(@NotNull XDebugSession session, DebuggerSession debuggerSession, DBJdwpTcpConfig tcpConfig);

    @NotNull
    protected <T> T assertNotNull(@Nullable T object, String message) throws ExecutionException {
        if (object == null) {
            throw new ExecutionException(message);
        }
        return object;
    }
}
