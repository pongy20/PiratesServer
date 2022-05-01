package de.coerdevelopment.standalone.serverui;

import de.coerdevelopment.standalone.logging.ServerLogger;

import javax.swing.*;
import java.sql.SQLException;

public class UITester {

    public static void main(String[] args) throws SQLException {

        ServerLogger.getInstance();   // important call

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            ServerLogger.getInstance().getLogger().error("Uncaught exception in thread {}.", t, e);
        });

        try {
            ServerStartUI startUI = ServerStartUI.createInstance();
            JFrame startFrame = startUI.createUI();

            startFrame.setVisible(true);
        } catch (Exception e) {
            ServerLogger.getInstance().getLogger().error(e);
        }


    }

}
