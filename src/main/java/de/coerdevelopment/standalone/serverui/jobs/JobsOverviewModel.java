package de.coerdevelopment.standalone.serverui.jobs;

import de.coerdevelopment.standalone.job.Jobrun;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class JobsOverviewModel extends AbstractTableModel {

    private List<Jobrun> displayedRuns;

    public JobsOverviewModel(List<Jobrun> initialRuns) {
        if (initialRuns == null) {
            displayedRuns = new ArrayList<>();
        } else {
            displayedRuns = initialRuns;
        }
    }

    public void setDisplayedRuns(List<Jobrun> displayedRuns) {
        this.displayedRuns = displayedRuns;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public int getRowCount() {
        return displayedRuns.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Jobname";
            case 1 -> "Startdate";
            case 2 -> "Starttime";
            case 3 -> "Runtime";
            case 4 -> "Status";
            default -> null;
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Jobrun run = displayedRuns.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> run.getJob().jobname;
            case 1 -> run.getFormattedStartDate();
            case 2 -> run.getFormattedStartTime();
            case 3 -> run.getRuntime();
            case 4 -> run.getStatus();
            default -> null;
        };
    }
}
