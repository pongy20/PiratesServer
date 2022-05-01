package de.coerdevelopment.standalone.job.instances.memory;

import de.coerdevelopment.standalone.job.Job;
import de.coerdevelopment.standalone.job.Jobrun;
import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.util.DebugMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MemoryUsageClearJob extends Job {

    private Server server;

    public MemoryUsageClearJob(Server server) {
        this(server, 1000 * 60 * 60 * 24);
    }
    public MemoryUsageClearJob(Server server, long interval) {
        super("M1001", true, interval);
        description = "Clears entries from memory usage job to reduce runtime storage.";
        this.server = server;
    }

    @Override
    public boolean execute(Jobrun jobrun) {
        if (getServerUpTime() < 1000 * 60 * 60 * 12 || server.memoryUsage.size() < 150) {
            return true;    // In this case the job will not be called because there is not enough data to clear
        }
        Set<Long> keys = server.memoryUsage.keySet();
        SortedSet<Long> timeKeys = new TreeSet<>(keys);
        Set<Long> deleteKeys = new HashSet<>();
        int i = 0;
        for(Long time : timeKeys) {
            if (i % 2 == 0) {
                deleteKeys.add(time);
            }
            i++;
        }
        for (Long time : deleteKeys) {
            server.memoryUsage.remove(time);
        }
        if (!deleteKeys.isEmpty()) {
            DebugMessage.sendJobMessage(deleteKeys.size() + " records have been cleared from memory usage!");
        }
        return true;
    }
    private long getServerUpTime() {
        return System.currentTimeMillis() - server.serverStartedTimeStamp;
    }

}
