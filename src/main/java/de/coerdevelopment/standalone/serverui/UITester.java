package de.coerdevelopment.standalone.serverui;

import de.coerdevelopment.pirates.authserver.AuthServer;
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
            ServerStartUI startUI = ServerStartUI.createInstance(new AuthServer());
            JFrame startFrame = startUI.createUI();

            startFrame.setVisible(true);
        } catch (Exception e) {
            ServerLogger.getInstance().getLogger().error(e);
        }


    }

}
