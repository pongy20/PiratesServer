package de.coerdevelopment.standalone.net.server;

import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;
import de.coerdevelopment.standalone.util.DebugMessage;

public class LoginServer extends Server {

    public static final int DEFAULT_PORT = 42201;

    public LoginServer(int port) {
        super(port, "Loginserver");
    }

    public LoginServer() {
        this(DEFAULT_PORT);
    }

    public void registerLoginMethods() {

    }

    @Override
    public void startServer() {
        tcpServer.start();
    }

}
