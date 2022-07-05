package de.coerdevelopment.standalone.util;

import de.coerdevelopment.standalone.net.server.Server;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ConsoleMessage implements ServerConsole {

    public SortedSet<ConsoleMessage> consoleMessages = new TreeSet<>(getConsoleComparator());

    private static ConsoleMessage instance;

    public static ConsoleMessage getInstance() {
        if (instance == null) {
            instance = new ConsoleMessage();
        }
        return instance;
    }

    public enum ConsoleMessageType{ERROR,DEBUG,WARNING,INFO,JOB};

    public ConsoleMessageType type;
    public Long time;
    public String message;

    public ConsoleMessage(ConsoleMessageType type, Long time, String message) {
        this.type = type;
        this.time = time;
        this.message = message;
    }

    private ConsoleMessage() {
    }

    private String getFormattedTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    public String toConsoleMessage() {
        String timePrefix = "[" + getFormattedTime() + "]";
        String prefix;

        switch (type) {
            case JOB -> prefix = "[Job]";
            case INFO -> prefix = "[Info]";
            case WARNING -> prefix = "[Warning]";
            case DEBUG -> prefix = "[Debug]";
            case ERROR -> prefix = "[Error]";
            default -> prefix = "[CONSOLE]";
        }
        return timePrefix + " " + prefix + " " + message;
    }

    @Override
    public String toString() {
        return toConsoleMessage();
    }

    @Override
    public Comparator<ConsoleMessage> getConsoleComparator() {
        return (o1, o2) -> o1.time < o2.time ? -1 : 1;
    }

    @Override
    public void addConsoleMessage(long time, String msg, ConsoleMessage.ConsoleMessageType type) {
        consoleMessages.add(new ConsoleMessage(type, time, msg));
    }
    @Override
    public void addConsoleMessage(long time, String msg, ConsoleMessage.ConsoleMessageType type, Server server) {
        consoleMessages.add(new ConsoleMessage(type, time, server.tcpServer.servername + " " + msg));
    }

    @Override
    public void clearConsole() {
        consoleMessages.clear();
    }

    /**
     * Returns all available Console Messages
     */
    @Override
    public SortedSet<ConsoleMessage> getAllConsoleMessages() {
        return consoleMessages;
    }

    /**
     * Returns all console messages matching the given ConsoleMessageType
     */
    public SortedSet<ConsoleMessage> getFilteredConsoleMessages(List<ConsoleMessageType> types) {
        SortedSet<ConsoleMessage> messages = new TreeSet<>(getConsoleComparator());
        for (ConsoleMessage msg : consoleMessages) {
            if (!types.contains(msg.type)) {
                continue;
            }
            messages.add(msg);
        }
        return messages;
    }

}
