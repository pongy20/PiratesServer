package de.coerdevelopment.standalone.job;

public enum JobrunStatus {

    CREATED("created"),
    PLANNED("planned"),
    STARTED("started"),
    RUNNING("running"),
    FINISHED_ERROR("finished (error)"),
    FINISHED_SUCCESSFUL("finished"),
    ABORT_USER ("abort (user)"),
    ABORT_SYSTEM("abort (system)");

    String name;

    JobrunStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
