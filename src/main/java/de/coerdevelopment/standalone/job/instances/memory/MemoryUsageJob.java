package de.coerdevelopment.standalone.job.instances.memory;

import de.coerdevelopment.standalone.job.Job;
import de.coerdevelopment.standalone.job.Jobrun;
import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.serverui.OverviewUI;

public class MemoryUsageJob extends Job {

    private Server server;
    private Runtime runtime;

    public MemoryUsageJob(Server server, int interval) {
        super("M1000", true, interval);
        description = "Job to save current memory usage for documentation.";
        this.server = server;
        runtime = Runtime.getRuntime();
    }

    @Override
    public boolean execute(Jobrun jobrun) {
        try {
            runtime.gc();   // clears unused memory space
            int usedMemory = (int) OverviewUI.bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
            server.memoryUsage.put(System.currentTimeMillis(), usedMemory);
            return true;
        } catch (Exception e) {
            jobrun.error = true;
            return false;
        }
    }

}
