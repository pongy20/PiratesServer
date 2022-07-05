package de.coerdevelopment.standalone.job;

import de.coerdevelopment.standalone.logging.ServerLogger;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public abstract class Job implements JobExecutable {

    public static List<Job> registeredJobs = new ArrayList<Job>();

    /**
     * Returns a list of most recent entries for selected jobname
     * @param jobname provide jobname | otherwise all jobs will be selected
     * @param maxEntries maximal entries to be selected
     */
    public static List<Jobrun> getRecentRuns(String jobname, int maxEntries) {
        if (maxEntries <= 0) {
            return new ArrayList<>();
        }
        List<Jobrun> availableRuns = new ArrayList<>();
        List<Jobrun> returningRuns = new ArrayList<>();
        if (jobname != null && getJobByName(jobname) != null) {      // specific job is selected
            availableRuns = getJobByName(jobname).jobRuns;
        } else {       // if there is no name provided --> returning recent runs of all available runs
            for (Job job : registeredJobs) {
                availableRuns.addAll(job.jobRuns);
            }
        }
        if (availableRuns.size() <= maxEntries) {    // the current job runs can be less than provided maxRuns --> return current runs
            return availableRuns;
        }
        for (Jobrun run : availableRuns) {
            if (returningRuns.size() < maxEntries) {
                returningRuns.add(run);
                continue;
            }
            Jobrun oldestRun = getOldestJobrunOfList(returningRuns);
            if (oldestRun.getStartTime() > run.getStartTime()) {
                returningRuns.remove(oldestRun);
                returningRuns.add(run);
            }
        }
        return returningRuns;
    }

    /**
     * Returns the job equals the provided jobname
     */
    public static Job getJobByName(String jobname) {
        for (Job job : registeredJobs) {
            if (job.jobname.equals(jobname)) {
                return job;
            }
        }
        return null;
    }

    private static Jobrun getOldestJobrunOfList(List<Jobrun> runs) {
        Jobrun currentOldest = null;
        for (Jobrun run : runs) {
            if (currentOldest == null) {
                currentOldest = run;
            } else {
                if (currentOldest.getStartTime() < run.getStartTime()) {
                    currentOldest = run;
                }
            }
        }
        return currentOldest;
    }

    private static boolean isJobRegistered(Job job) {
        for (Job temp : registeredJobs) {
            if (temp.jobname.equals(job.jobname)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Maximal duration the job should wait till it will be canceled
     */
    public long maxDuration;
    /**
     * The interval between the next run (if it is a repeating job)
     */
    public long interval;
    /**
     * Boolean if the job should be repeated
     * An interval time have to be set if the job should be repeated
     */
    public boolean repeating;

    protected Thread timerThread;
    protected Timer repeatingTimer;

    public final String jobname;
    public String description;

    private List<Jobrun> jobRuns;

    public Job(String jobname, boolean repeating, long interval) {
        this.jobname = jobname;
        this.repeating = repeating;
        this.interval = interval;
        this.jobRuns = new ArrayList<>();

        if (!isJobRegistered(this)) registeredJobs.add(this);
    }

    /**
     * Starts the job for one time
     */
    public void startJob() {
        Jobrun run = new Jobrun(this);
        run.start();
    }

    /**
     * Repeats a single job-start in given interval
     */
    public void startRepeatingJob() {
        if (timerThread != null && !timerThread.isInterrupted()) {
            return; // repeating job is already running
        }
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                repeatingTimer = new Timer();
                repeatingTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        startJob();
                    }
                }, 0, interval);
            }
        });
        timerThread.setUncaughtExceptionHandler((t, e) -> {
            ServerLogger.getInstance().getJobLogger().error("Uncaught exception in thread {}.", t, e);
        });
        timerThread.start();
    }

    /**
     * Cancels all active runs
     * New runs will continue starting
     */
    public void cancelAllRuns() {
        if (!jobRuns.isEmpty()) {
            for (Jobrun run : jobRuns) {
                run.stop(false);
            }
        }
    }

    public void cancelRepeatingAndAllRuns() {
        cancelAllRuns();
        if (repeatingTimer != null && timerThread != null) {
            repeatingTimer.cancel();
            timerThread.interrupt();
            timerThread = null;
            repeatingTimer = null;
        }

    }

    public JDialog getJobDetailsDialog() {
        JDialog dialog = new JDialog();
        dialog.setContentPane(getJobDetailsPanel());
        return dialog;
    }

    public JPanel getJobDetailsPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JLabel headerLabel = new JLabel(jobname + ":");
        headerLabel.setFont(new Font("Arial", 0, 35));
        JLabel nameLabel = new JLabel("Jobname: " + jobname);
        JLabel descriptionLabel = new JLabel("Description: " + description);
        JLabel intervalLabel = new JLabel("Interval: " + interval);
        JLabel repeatingLabel = new JLabel("Repeating: " + repeating);
        JLabel maxDurationLabel = new JLabel("Maximal Duration: " + maxDuration);

        List<Component> components = Arrays.asList(headerLabel, nameLabel, descriptionLabel,
                intervalLabel, repeatingLabel, maxDurationLabel);

        GridBagConstraints constraints = new GridBagConstraints();
        int currentY = 1;

        for (Component comp : components) {
            panel.add(comp, constraints);
            currentY++;
            constraints.gridy = currentY;
        }

        return panel;
    }
    @Override
    public String toString() {
        return "[" + jobname + "]" + " running: "  + " repeating: " + repeating;
    }

    public List<Jobrun> getJobRuns() {
        return jobRuns;
    }
    public void addJobrun(Jobrun jobrun) {
        if (jobrun != null) {
            jobRuns.add(jobrun);
        }
    }
    public void removeJobrun(Jobrun run) {
        jobRuns.remove(run);
    }
}
