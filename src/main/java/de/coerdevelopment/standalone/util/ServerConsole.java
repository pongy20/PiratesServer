package de.coerdevelopment.standalone.util;

import de.coerdevelopment.standalone.net.server.Server;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface ServerConsole {

    /**
     * Adds a new Console Message to server console
     * @param time - The time when the message was logged
     * @param msg - The console message itself
     * @param type - The kind of console message
     */
    void addConsoleMessage(long time, String msg, ConsoleMessage.ConsoleMessageType type);
    /**
     * Adds a new Console Message to server console
     * @param time - The time when the message was logged
     * @param msg - The console message itself
     * @param type - The kind of console message
     * @param server - server to add the servername
     */
    void addConsoleMessage(long time, String msg, ConsoleMessage.ConsoleMessageType type, Server server);

    /**
     * Clears the server console
     */
    void clearConsole();

    /**
     * @return Returns all available ConsoleMessages
     */
    Set<ConsoleMessage> getAllConsoleMessages();

    /**
     *
     * @param types - The ConsoleMessage.ConsoleMessageType the console messages includes
     * @return Filters all available ConsoleMessage instances using the provided ConsoleMessageType
     */
    SortedSet<ConsoleMessage> getFilteredConsoleMessages(List<ConsoleMessage.ConsoleMessageType> types);

    /**
     * @return The console comparator to order the messages
     */
    Comparator<ConsoleMessage> getConsoleComparator();


}
