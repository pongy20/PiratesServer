package de.coerdevelopment.standalone.util;

import de.coerdevelopment.standalone.logging.ServerLogger;
import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.serverui.OverviewUI;
import org.apache.logging.log4j.Logger;

public final class DebugMessage {
    //ERROR,DEBUG,WARNING,INFO,JOB
    private static ServerConsole console;
    private static Server server;
    private static OverviewUI overviewUI;
    private static Logger logger, jobLogger;


    public static synchronized void sendErrorMessage(Object msg) {
        if (msg != null) {
            sendMessage(msg.toString(), ConsoleMessage.ConsoleMessageType.ERROR);
            logger.error(msg.toString());
        }
    }
    public static synchronized void sendWarningMessage(Object msg) {
        if (msg != null) {
            sendMessage(msg.toString(), ConsoleMessage.ConsoleMessageType.WARNING);
            logger.warn(msg.toString());
        }
    }
    public static synchronized void sendDebugMessage(Object msg) {
        if (msg != null) {
            sendMessage(msg.toString(), ConsoleMessage.ConsoleMessageType.DEBUG);
            logger.debug(msg.toString());
        }
    }
    public static synchronized void sendInfoMessage(Object msg) {
        if (msg != null) {
            sendMessage(msg.toString(), ConsoleMessage.ConsoleMessageType.INFO);
            logger.info(msg.toString());
        }
    }
    public static synchronized void sendJobMessage(Object msg) {
        if (msg != null) {
            sendMessage(msg.toString(), ConsoleMessage.ConsoleMessageType.JOB);
            jobLogger.info(msg.toString());
        }
    }

    public static synchronized void sendMessage(String msg, ConsoleMessage.ConsoleMessageType type) {
        if (console == null) {
            console = ConsoleMessage.getInstance();
        }
        if (server == null) {
            server = Server.instance;
        }
        if (overviewUI == null) {
            overviewUI = OverviewUI.getInstance();
        }
        if (logger == null || jobLogger == null) {
            logger = ServerLogger.getInstance().getLogger();
            jobLogger = ServerLogger.getInstance().getJobLogger();
        }

        if (console != null) {
            if (server != null) {
                console.addConsoleMessage(System.currentTimeMillis(), msg, type, server);
            } else {
                console.addConsoleMessage(System.currentTimeMillis(), msg, type);
            }
            if (overviewUI != null) {
                overviewUI.updateConsole();
            }
        } else {
            System.out.println(msg);
        }
    }
}
