package de.coerdevelopment.standalone.net.tcp;

import de.coerdevelopment.standalone.util.DebugMessage;

import java.io.IOException;
import java.net.Socket;

public class TcpClientThread extends TcpThread {

    private final TcpServer server;
    public Player player;

    public TcpClientThread(final TcpServer server, Socket client) {
        super(client, server.registeredMethods);
        this.server = server;
    }

    @Override
    public void closeConnection() {
        DebugMessage.sendDebugMessage("Client " + socket.getInetAddress().getHostAddress() + " disconnected");
        server.removeClient(this);
        handleLobbyDisconnect();
        try {
            socket.close();
        } catch (IOException e) {
            DebugMessage.sendErrorMessage(e);
        }
        interrupt();
    }

    /**
     * Handles if the disconnect effects any lobby issues
     */
    private void handleLobbyDisconnect() {
        GameLobby lobby = WizzGameServer.getInstance().getLobbyByClient(this);
        if (lobby == null) {        // player is not registered in any lobby
            return;
        }
        //TODO: Unterscheiden in welcher GamePhase sich das Spiel befindet
        TcpMethod playerLeftMethod = new GetPlayerLeftLobbyMethod();
        playerLeftMethod.onMethod(null, this);
    }

}
