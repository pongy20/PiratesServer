package de.coerdevelopment.standalone.job;

public interface JobExecutable {

    /**
     * Will be executed by job
     * @return true if the job run was successful, false if an error occurred
     */
    boolean execute(Jobrun jobrun);

}
