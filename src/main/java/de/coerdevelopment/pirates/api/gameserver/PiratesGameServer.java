package de.coerdevelopment.pirates.api.gameserver;

import de.coerdevelopment.pirates.api.gameserver.methods.LoginPlayerMethod;
import de.coerdevelopment.standalone.net.server.GameServer;

public class PiratesGameServer extends GameServer {

    public PiratesGameServer(int port) {
        super(port);
        registerMethods();
    }

    public PiratesGameServer() {
        super();
        registerMethods();
    }

    public void registerMethods() {
        tcpServer.registerMethod(new LoginPlayerMethod());
    }
}
