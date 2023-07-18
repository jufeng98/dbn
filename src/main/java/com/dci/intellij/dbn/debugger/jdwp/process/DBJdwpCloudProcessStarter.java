package com.dci.intellij.dbn.debugger.jdwp.process;

import com.dci.intellij.dbn.common.dispose.Failsafe;
import com.dci.intellij.dbn.connection.ConnectionHandler;
import com.dci.intellij.dbn.connection.config.ConnectionPropertiesSettings;
import com.dci.intellij.dbn.connection.ConnectionUtil;
import com.dci.intellij.dbn.debugger.common.config.DBRunConfig;
import com.dci.intellij.dbn.debugger.jdwp.config.DBJdwpRunConfig;
import com.dci.intellij.dbn.debugger.jdwp.process.tunnel.NSTunnelConnectionProxy;
import com.dci.intellij.dbn.debugger.jdwp.process.tunnel.NSTunnelIO;
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
import com.jetbrains.jdi.GenericAttachingConnector;
import com.jetbrains.jdi.SocketTransportService;
import com.jetbrains.jdi.VirtualMachineManagerImpl;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.spi.Connection;
//import oracle.net.ns.NSTunnelConnection;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Driver;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public abstract class DBJdwpCloudProcessStarter extends DBJdwpProcessStarter{

    private  String jdwpHostPort = null;
    private NSTunnelConnectionProxy debugConnection = null;
    ByteBuffer readBuffer = ByteBuffer.allocate(320000);
    ByteBuffer writeBuffer = ByteBuffer.allocate(320000);

    DBJdwpCloudProcessStarter(ConnectionHandler connection) {
        super(connection);
    }






    void connect() throws IOException {
        if (debugConnection != null) {
            try {
                debugConnection.close();
            } catch (Exception e) {
                throw e;
            }
        }
        Properties props = new Properties();
        String URL = getConnection().getSettings().getDatabaseSettings().getConnectionUrl();
//        debugConnection = NSTunnelConnection.newInstance(URL, props);
        try {
            Driver driver = ConnectionUtil.resolveDriver(getConnection().getSettings().getDatabaseSettings());
            debugConnection = NSTunnelIO.newInstance(driver.getClass().getClassLoader(), URL, props);
            System.out.println("Connect = " + debugConnection.tunnelAddress());
        } catch (final Exception e) {
        	e.printStackTrace();
        }
        

       jdwpHostPort = debugConnection.tunnelAddress();
       ConnectionPropertiesSettings connectionSettings  =  getConnection().getSettings().getPropertiesSettings();
       connectionSettings.getProperties().put("jdwpHostPort",jdwpHostPort);
    }


    /**
     * cloud database start's implementation : use attach connector . and using reflection to override
     * the behavior of attach connector to use the NSTunnelConnection instead of establish new connection
     * between debugger and database
     * @param session session to be passed to {@link XDebugProcess#XDebugProcess} constructor
     * @return
     * @throws ExecutionException
     */
    @Override
    public @NotNull XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
        fixSocketConnectors();
        try {
            connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Executor executor = DefaultDebugExecutor.getDebugExecutorInstance();
        RunProfile runProfile = session.getRunProfile();
        assertNotNull(runProfile,"invalid run profile");



        ExecutionEnvironment environment = ExecutionEnvironmentBuilder.create(session.getProject(),executor,runProfile).build();
        String debugHostName = extractHost(jdwpHostPort);
        String port = extractPort(jdwpHostPort);
        RemoteConnection remoteConnection = new RemoteConnection(true,debugHostName,port,false);


        RunProfileState state = Failsafe.nn(runProfile.getState(executor, environment));

        DebugEnvironment debugEnvironment = new DefaultDebugEnvironment(environment, state, remoteConnection, true);
        DebuggerManagerEx debuggerManagerEx = DebuggerManagerEx.getInstanceEx(session.getProject());
        DebuggerSession debuggerSession = debuggerManagerEx.attachVirtualMachine(debugEnvironment);
        assertNotNull(debuggerSession, "Could not initialize JDWP listener");


        return createDebugProcess(session, debuggerSession, debugHostName, Integer.valueOf(port) );

    }
    public static String extractHost(String input) {
        int hostStartIndex = input.indexOf("host=") + 5;
        int hostEndIndex = input.indexOf(";port=");
        return input.substring(hostStartIndex, hostEndIndex);
    }

    public static String extractPort(String input) {
        int portStartIndex = input.indexOf("port=") + 5;
        return input.substring(portStartIndex);
    }

    private void fixSocketConnectors() {
        VirtualMachineManagerImpl vmm = VirtualMachineManagerImpl.virtualMachineManager();
        Optional<AttachingConnector> curConnector =
                vmm.attachingConnectors().stream().filter(l -> l.name().equals("com.jetbrains.jdi.SocketAttach")).findFirst();
        curConnector.ifPresentOrElse(
                c -> {

                    Class<? extends GenericAttachingConnector> class1 =
                            GenericAttachingConnector.class;
                    Field declaredField = null;
                    try {
                        declaredField = class1.getDeclaredField("transportService");
                    } catch (NoSuchFieldException | SecurityException e) {
                        // TODO Auto-generated catch block
                        throw new RuntimeException(e);
                    }
                    declaredField.setAccessible(true);
                    SocketTransportService sts = new SocketTransportService() {

                        @Override
                        public Connection attach(String address, long attachTimeout, long handshakeTimeout) throws IOException {
                            doHandCheck();

                            Connection foo = new Connection() {


                                @Override
                                public byte[] readPacket() throws IOException {
                                    byte[] packet = readPackets();
                                    return packet;
                                }

                                @Override
                                public void writePacket(byte[] pkt) throws IOException {
                                    writePackets(pkt);
                                }

                                @Override
                                public void close() throws IOException {
                                    debugConnection.close();

                                }

                                @Override
                                public boolean isOpen() {
                                    try {
                                        return debugConnection.isOpen();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
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
                () -> {
                    throw new RuntimeException("Expected to find listening connector");
                });
    }

    void doHandCheck() throws IOException {
        System.out.println("handshake starts ...........");
        byte[] hello = "JDWP-Handshake".getBytes(StandardCharsets.UTF_8);
        readBuffer.clear();
        writeBuffer.clear();
        debugConnection.read(readBuffer);
        byte[] hello_read = new byte[hello.length];
        readBuffer.get(hello_read);
        if (Arrays.compare(hello, hello_read) == 0) {
            System.out.println("handshake not done");
        }
        writePackets(hello);
        readBuffer.clear();

        System.out.println("handshake finishes ...........");
    }

    // read just one packet at each time called
    // and buffer the rest
    byte[] readPackets() throws IOException {
        if (readBuffer.position() > 0) {
            // the buffer contains incomplete packet
            int packetLength = readBuffer.getInt(0);
            while(readBuffer.position() < packetLength) {
                debugConnection.read(readBuffer);
            }

            readBuffer.flip();
            byte[] packet = new byte[packetLength];
            readBuffer.get(packet);

            if (readBuffer.hasRemaining()) {
                byte[] extra = new byte[readBuffer.limit() - readBuffer.position()];
                readBuffer.get(extra);
                readBuffer.clear();
                readBuffer.put(extra);
            } else {
                readBuffer.clear();
            }

            return packet;
        }

        readBuffer.clear();
        debugConnection.read(readBuffer);
        return readPackets();

    }


    synchronized void writePackets(byte[] bytes) throws IOException {
        writeBuffer.clear();
        writeBuffer.put(bytes);
        writeBuffer.flip();
        debugConnection.write(writeBuffer);
    }
}
