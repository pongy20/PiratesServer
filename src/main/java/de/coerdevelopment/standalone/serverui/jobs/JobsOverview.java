package de.coerdevelopment.standalone.serverui.jobs;

import de.coerdevelopment.standalone.job.Job;
import de.coerdevelopment.standalone.job.Jobrun;
import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.serverui.jobs.showjob.ShowJobUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JobsOverview {
    public JPanel contentPanel;
    private JPanel headerPanel;
    private JPanel centerPanel;
    private JScrollPane tableScrollPane;
    public JTable jobsTable;
    public JComboBox jobsComboBox;
    private JButton detailsButton;
    private JButton refreshButton;
    private JPanel footerPanel;
    private JComboBox maxEntryCombo;
    private JLabel maxEntriesLabel;

    public JobsOverviewModel tableModel;
    private Server server;

    public JobsOverview(Server server) {
        this.server = server;
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Job selectedJob = Job.getJobByName(jobsComboBox.getSelectedItem().toString());
                if (selectedJob == null) {
                    return;
                }
                JDialog jobDialog = ShowJobUI.getJobDetailDialog(selectedJob, JobsOverview.this.contentPanel);
                jobDialog.setVisible(true);
            }
        });
    }

    private void createUIComponents() {
        jobsTable = new JTable();
        tableModel = new JobsOverviewModel(Job.getRecentRuns(null, 50));
        jobsTable.setModel(tableModel);


        List<String> jobnames = new ArrayList<>();
        jobnames.add(" ");
        for (Job job : Job.registeredJobs) {
            jobnames.add(job.jobname);
        }

        jobsComboBox = new JComboBox(jobnames.toArray());
    }

    public void updateTable() {
        String selectedJobname = jobsComboBox.getSelectedItem().toString();
        int maxEntries = Integer.parseInt(maxEntryCombo.getSelectedItem().toString());
        List<Jobrun> displayedRuns = Job.getRecentRuns(selectedJobname, maxEntries);
        tableModel.setDisplayedRuns(displayedRuns);
        tableModel.fireTableDataChanged();
    }

}
