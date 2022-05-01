package de.coerdevelopment.standalone.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class ConsoleMessage {

    public enum ConsoleMessageType{ERROR,DEBUG,WARNING,INFO,JOB};

    public ConsoleMessageType type;
    public Long time;
    public String message;

    public ConsoleMessage(ConsoleMessageType type, Long time, String message) {
        this.type = type;
        this.time = time;
        this.message = message;
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

}
