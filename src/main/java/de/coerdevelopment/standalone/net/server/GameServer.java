package de.coerdevelopment.standalone.net.server;

import de.coerdevelopment.standalone.repository.SQL;

import java.sql.SQLException;

public class GameServer extends Server {

    public static final int DEFAULT_PORT = 42210;

    public GameServer(int port) {
        super(port, "Gameserver");
        maxOnlinePlayers = 1000;

        if (false) {
            initSQL();
        }
    }

    public GameServer() {
        this(DEFAULT_PORT);
    }

    private void initSQL() {
        //TODO: change sql settings!
        SQL.newSQL("localhost", "wizz", "wizz123", "wizz", "3306");
        try {
            SQL.getSQL().connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startServer() {
        tcpServer.start();
    }

}
