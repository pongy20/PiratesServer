package de.coerdevelopment.standalone.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class ServerLogger {

    private static ServerLogger instance;

    public static ServerLogger getInstance() {
        if (instance == null) {
            instance = new ServerLogger();
        }
        return instance;
    }

    private Logger logger;
    private Logger jobLogger;

    private ServerLogger() {
        logger = LogManager.getLogger("ServerLogger");
        jobLogger = LogManager.getLogger("JobLogger");
        Configurator.initialize("ServerLogger", "src/log4j2.xml");
        Configurator.initialize("JobLogger", "src/joblogger.xml");
    }

    public Logger getJobLogger() {
        return jobLogger;
    }

    public Logger getLogger() {
        return logger;
    }
}