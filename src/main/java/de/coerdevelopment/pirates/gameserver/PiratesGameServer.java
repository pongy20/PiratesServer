package de.coerdevelopment.pirates.gameserver;

import de.coerdevelopment.pirates.gameserver.methods.LoginPlayerMethod;
import de.coerdevelopment.standalone.net.server.GameServer;
import de.coerdevelopment.standalone.repository.SQL;
import de.coerdevelopment.standalone.util.DebugMessage;

public class PiratesGameServer extends GameServer {

    public PiratesGameServer(int port) {
        super(port);
        initSQL();
        registerMethods();
    }

    public PiratesGameServer() {
        super();
        registerMethods();
    }

    public void registerMethods() {
        tcpServer.registerMethod(new LoginPlayerMethod());
    }

    private void initSQL() {
        SQL sql = SQL.newSQL("localhost", "pirates_anson", "pirates", "pirates_anson", "3306");
        if (sql.initSQL()) {
            DebugMessage.sendInfoMessage("SQL-Connection successful!");
        }
    }
}
