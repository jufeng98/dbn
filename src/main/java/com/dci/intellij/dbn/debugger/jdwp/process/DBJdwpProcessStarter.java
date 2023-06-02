package com.dci.intellij.dbn.debugger.jdwp.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.common.util.Strings;
import com.dci.intellij.dbn.connection.ConnectionHandler;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import com.jetbrains.jdi.GenericListeningConnector;
import com.jetbrains.jdi.SocketTransportService;
import com.jetbrains.jdi.VirtualMachineManagerImpl;
import com.sun.jdi.connect.ListeningConnector;
import com.sun.jdi.connect.spi.Connection;

import static com.dci.intellij.dbn.diagnostics.Diagnostics.conditionallyLog;

@Slf4j
public abstract class DBJdwpProcessStarter extends DBDebugProcessStarter {

    public static final Key<Integer> JDWP_DEBUGGER_PORT = new Key<>("JDWP_DEBUGGER_PORT");

    DBJdwpProcessStarter(ConnectionHandler connection) {
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

    @NotNull
    @Override
    public final XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
    	fixSocketConnectors();
        Executor executor = DefaultDebugExecutor.getDebugExecutorInstance();
        RunProfile runProfile = session.getRunProfile();
        assertNotNull(runProfile, "Invalid run profile");

        ExecutionEnvironment environment = ExecutionEnvironmentBuilder.create(session.getProject(), executor, runProfile).build();
        DBJdwpRunConfig jdwpRunConfig = (DBJdwpRunConfig) runProfile;
        Range<Integer> portRange = jdwpRunConfig.getTcpPortRange();
        String tcpHost = resolveTcpHost(jdwpRunConfig);
        int tcpPort = findFreePort(tcpHost, portRange.getFrom(), portRange.getTo());


        RemoteConnection remoteConnection = new RemoteConnection(true, tcpHost, Integer.toString(tcpPort), true);

        RunProfileState state = Failsafe.nn(runProfile.getState(executor, environment));

        DebugEnvironment debugEnvironment = new DefaultDebugEnvironment(environment, state, remoteConnection, true);
        DebuggerManagerEx debuggerManagerEx = DebuggerManagerEx.getInstanceEx(session.getProject());
        DebuggerSession debuggerSession = debuggerManagerEx.attachVirtualMachine(debugEnvironment);
        assertNotNull(debuggerSession, "Could not initialize JDWP listener");

        return createDebugProcess(session, debuggerSession, tcpHost, tcpPort);
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

    private void fixSocketConnectors() {
    	VirtualMachineManagerImpl vmm = VirtualMachineManagerImpl.virtualMachineManager();
    	Optional<ListeningConnector> curConnector = 
    		vmm.listeningConnectors().stream().filter(l -> l.name().equals("com.jetbrains.jdi.SocketListen")).findFirst();
    	curConnector.ifPresentOrElse(
    		c -> {
//    			vmm.removeConnector(c);
//    			SocketListeningConnector newConnector = new SocketListeningConnector();
    			Class<? extends GenericListeningConnector> class1 = 
    				GenericListeningConnector.class;
    			Field declaredField = null;
				try {
					declaredField = class1.getDeclaredField("transportService");
				} catch (NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			declaredField.setAccessible(true);
    			SocketTransportService sts = new SocketTransportService() {

					@Override
					public Connection accept(ListenKey key, long acceptTimeout, long handshakeTimeout) throws IOException {
						final Connection delegate = super.accept(key, acceptTimeout, handshakeTimeout);
						Connection foo = new Connection() {

							@Override
							public byte[] readPacket() throws IOException {
								byte[] packet = delegate.readPacket();
								System.out.printf("Reading JDWP data. %d bytes\n", packet.length);
								Packet p = Packet.read(new ByteArrayInputStream(packet));
								System.out.println(p.toString());
								return packet;
							}

							@Override
							public void writePacket(byte[] pkt) throws IOException {
								delegate.writePacket(pkt);
								System.out.printf("Writing JDWP data. %d bytes\n", pkt.length);
								Packet p = Packet.read(new ByteArrayInputStream(pkt));
								System.out.println(p.toString());
							}

							@Override
							public void close() throws IOException {
								delegate.close();
							}

							@Override
							public boolean isOpen() {
								return delegate.isOpen();
							}
							
						};
						return foo;
					}
    				
    			};
    			try {
					declaredField.set(c, sts);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}, 
    		() -> {throw new RuntimeException("Expected to find listening connector");});
    }
    

	protected abstract DBJdwpDebugProcess createDebugProcess(@NotNull XDebugSession session, DebuggerSession debuggerSession, String hostname, int tcpPort);

    private @NotNull <T> T assertNotNull(@Nullable T object, String message) throws ExecutionException {
        if (object == null) {
            throw new ExecutionException(message);
        }
        return object;
    }
}
