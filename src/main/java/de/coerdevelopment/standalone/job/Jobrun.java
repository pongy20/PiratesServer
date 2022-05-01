package de.coerdevelopment.standalone.job;

import de.coerdevelopment.standalone.logging.ServerLogger;
import de.coerdevelopment.standalone.util.TimeCalculator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Jobrun {

    private final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final DateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private Job job;
    private final long startTime;
    public final long createdTime;
    private long endTime;
    protected Thread jobThread;
    public boolean running;
    public boolean error;
    public JobrunStatus status;
    private Timer startTimeTimer;

    public Jobrun(Job job, final long startTime) {
        this.job = job;
        this.startTime = startTime;
        this.createdTime = System.currentTimeMillis();
        status = JobrunStatus.CREATED;
        jobThread = new Thread(new Runnable() {
            @Override
            public void run() {
                status = JobrunStatus.RUNNING;
                error = !job.execute(Jobrun.this);      // executes the job, sets the error variable at the same time
                running = false;
                status = error ? JobrunStatus.FINISHED_ERROR : JobrunStatus.FINISHED_SUCCESSFUL;
                endTime = System.currentTimeMillis();
            }
        });
        jobThread.setUncaughtExceptionHandler((t, e) -> {
            ServerLogger.getInstance().getJobLogger().error("Uncaught exception in thread {}.", t, e);
        });
        job.addJobrun(this);
    }
    public Jobrun(Job job) {
        this(job, System.currentTimeMillis());
    }

    /**
     * Startes the run instantly after calling this method
     */
    public void start() {
        status = JobrunStatus.STARTED;
        running = true;
        jobThread.start();
    }

    /**
     * Starts the run when the given start time is reached
     */
    public void startOnStartTime() {
        status = JobrunStatus.PLANNED;
        Date date = new Date(startTime);
        startTimeTimer = new Timer();
        startTimeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                start();
            }
        }, date);
    }

    /**
     * @param userStop specifies if the user stopped the job run
     */
    public void stop(boolean userStop) {
        if (status.equals(JobrunStatus.PLANNED)) {
            startTimeTimer.cancel();
        }
        jobThread.interrupt();
        if (userStop) {
            status = JobrunStatus.ABORT_USER;
        } else {
            status = JobrunStatus.ABORT_SYSTEM;
        }
    }

    public long getStartTime() {
        return startTime;
    }
    public String getFormattedStartTime() {
        return timeFormat.format(new Date(startTime));
    }
    public String getFormattedStartDate() {
        return dateFormat.format(new Date(startTime));
    }
    public String getFormattedStartDateTime() {
        return dateTimeFormat.format(new Date(startTime));
    }
    public String getRuntime() {
        long runtime;
        if (running) {
            runtime = System.currentTimeMillis() - startTime;
        } else {
            runtime = endTime - startTime;
            if (runtime < 0) {
                runtime = 0;
            }
        }
        return String.valueOf(TimeCalculator.getInstance().convertTime(runtime));
    }
    public String getStatus() {
        return status.toString();
    }
    public Job getJob() {
        return job;
    }

}