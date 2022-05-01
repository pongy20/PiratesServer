package de.coerdevelopment.standalone.job.instances.util;

import de.coerdevelopment.standalone.job.Job;
import de.coerdevelopment.standalone.job.Jobrun;
import de.coerdevelopment.standalone.job.JobrunStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClearCreatedJobsJob extends Job {

    /**
     * The time in milliseconds a job maximal can last in created phase after this job clears it
     */
    private final long timeToClear = 1000 * 60 * 5;

    public ClearCreatedJobsJob(long interval) {
        super("J1000", true, interval);
        description = "Clears all jobs in status 'created'";
    }

    @Override
    public boolean execute(Jobrun jobrun) {
        long timeToKill = System.currentTimeMillis() - timeToClear;
        Map<Job, List<Jobrun>> toDelete = new HashMap<>();
        for (Job job : Job.registeredJobs) {
            List<Jobrun> jobrunsToDelete = new ArrayList<>();
            for (Jobrun currentRun : job.getJobRuns()) {
                if (currentRun.status.equals(JobrunStatus.CREATED)) {
                    if (currentRun.createdTime < timeToKill) {
                        jobrun.status = JobrunStatus.ABORT_SYSTEM;
                        jobrunsToDelete.add(currentRun);
                    }
                }
            }
            if (!jobrunsToDelete.isEmpty()) {
                toDelete.put(job, jobrunsToDelete);
            }
        }
        if (!toDelete.isEmpty()) {
            for (Map.Entry<Job, List<Jobrun>> pair : toDelete.entrySet()) {
                for (Jobrun run : pair.getValue()) {
                    pair.getKey().removeJobrun(run);
                }
            }
        }
        return true;
    }

}
