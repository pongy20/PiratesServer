package de.coerdevelopment.standalone.serverui;

import de.coerdevelopment.standalone.logging.ServerLogger;
import de.coerdevelopment.standalone.net.server.GameServer;
import de.coerdevelopment.standalone.net.server.LoginServer;
import de.coerdevelopment.standalone.net.server.ServerType;

import javax.swing.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UITester {

    public static void main(String[] args) throws SQLException {

        ServerLogger.getInstance();   // important call

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            ServerLogger.getInstance().getLogger().error("Uncaught exception in thread {}.", t, e);
        });

        try {
            Map<String, Object[]> servers = new HashMap<>();
            servers.put("Auth-Server", new Object[]{ServerType.LOGINSERVER, LoginServer.DEFAULT_PORT});
            servers.put("GameServer", new Object[]{ServerType.GAMESERVER, GameServer.DEFAULT_PORT});
            ServerStartUI startUI = ServerStartUI.createInstance(servers);
            JFrame startFrame = startUI.createUI(servers);

            startFrame.setVisible(true);
        } catch (Exception e) {
            ServerLogger.getInstance().getLogger().error(e);
        }


    }

}
