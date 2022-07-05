package de.coerdevelopment.pirates;

import de.coerdevelopment.pirates.authserver.AuthServer;
import de.coerdevelopment.standalone.logging.ServerLogger;
import de.coerdevelopment.standalone.net.server.GameServer;
import de.coerdevelopment.standalone.net.server.LoginServer;
import de.coerdevelopment.standalone.net.server.ServerType;
import de.coerdevelopment.standalone.repository.SQL;
import de.coerdevelopment.standalone.serverui.ServerStartUI;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class PiratesServerStart {

    public static void main(String[] args) {
        ServerLogger.getInstance();   // important call

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            ServerLogger.getInstance().getLogger().error("Uncaught exception in thread {}.", t, e);
        });

        try {
            SQL.newSQL("localhost", "pirates", "pirates", "pirates", "3306");
            SQL.getSQL().connect();
            if (!SQL.getSQL().isConnected()) {
                System.out.println("No SQL connection");
                return;
            }
            ServerStartUI startUI = ServerStartUI.createInstance(new AuthServer());
            JFrame startFrame = startUI.createUI();

            startFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            ServerLogger.getInstance().getLogger().error(e);
        }
    }

}
