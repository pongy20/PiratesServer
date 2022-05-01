package de.coerdevelopment.standalone.net.server;

import de.coerdevelopment.standalone.job.Job;
import de.coerdevelopment.standalone.job.instances.memory.MemoryUsageClearJob;
import de.coerdevelopment.standalone.job.instances.memory.MemoryUsageJob;
import de.coerdevelopment.standalone.job.instances.util.ClearCreatedJobsJob;
import de.coerdevelopment.standalone.net.tcp.TcpServer;
import de.coerdevelopment.standalone.util.ConsoleMessage;
import de.coerdevelopment.standalone.util.DebugMessage;
import de.coerdevelopment.standalone.util.ServerConsole;

import java.util.*;

/**
 * Abstract server class
 * Have to be implemented by ex. Loginserver, Gameserver
 * Provides Server-Console and TcpServer
 */
public abstract class Server implements ServerConsole {

    /**
     * Server instance for Debug Messages
     */
    public static Server instance;

    public TcpServer tcpServer;
    protected SortedSet<ConsoleMessage> consoleMessages;

    /**
     * Saves the used memory over time
     * Key - Saves the time
     * Value - The amount of used memory
     */
    public Map<Long, Integer> memoryUsage;

    public long serverStartedTimeStamp;

    /**
     * Limits the maximum amount of players being online
     * at the same time to avoid performance issues
     */
    public int maxOnlinePlayers;

    public Server(int port, String servername) {
        tcpServer = new TcpServer(port, servername);
        maxOnlinePlayers = 1000;
        consoleMessages = new TreeSet<>(getConsoleComparator());
        memoryUsage = new HashMap<>();
        serverStartedTimeStamp = System.currentTimeMillis();

        registerJobs();
        startJobs();

        instance = this;
    }

    public abstract void startServer();

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
    public SortedSet<ConsoleMessage> getFilteredConsoleMessages(List<ConsoleMessage.ConsoleMessageType> types) {
        SortedSet<ConsoleMessage> messages = new TreeSet<>(getConsoleComparator());
        for (ConsoleMessage msg : consoleMessages) {
            if (!types.contains(msg.type)) {
                continue;
            }
            messages.add(msg);
        }
        return messages;
    }

    public Comparator<ConsoleMessage> getConsoleComparator() {
        return (o1, o2) -> o1.time < o2.time ? -1 : 1;
    }

    protected void registerJobs() {
        new MemoryUsageJob(this, 1000 * 60 * 30);
        new MemoryUsageClearJob(this);
        new ClearCreatedJobsJob(1000 * 60 * 60);
    }
    protected void startJobs() {
        for (Job job : Job.registeredJobs) {
            if (job.repeating) {
                job.startRepeatingJob();
            } else {
                job.startJob();
            }
        }
    }

}
