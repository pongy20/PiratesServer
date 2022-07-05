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
public abstract class Server {

    /**
     * Server instance for Debug Messages
     */
    public static Server instance;

    public TcpServer tcpServer;

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
        memoryUsage = new HashMap<>();
        serverStartedTimeStamp = System.currentTimeMillis();

        registerJobs();
        startJobs();
    }

    public abstract void startServer();

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
