package de.coerdevelopment.standalone.net.tcp;

import de.coerdevelopment.standalone.util.DebugMessage;

import java.io.IOException;
import java.net.Socket;

public class TcpClientThread extends TcpThread {

    private final TcpServer server;

    public TcpClientThread(final TcpServer server, Socket client) {
        super(client, server.registeredMethods);
        this.server = server;
    }

    @Override
    public void closeConnection() {
        DebugMessage.sendDebugMessage("Client " + socket.getInetAddress().getHostAddress() + " disconnected");
        server.removeClient(this);
        try {
            socket.close();
        } catch (IOException e) {
            DebugMessage.sendErrorMessage(e);
        }
        interrupt();
    }

}
