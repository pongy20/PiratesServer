package de.coerdevelopment.standalone.serverui.jobs.showjob;

import de.coerdevelopment.standalone.job.Job;
import de.coerdevelopment.standalone.job.Jobrun;
import de.coerdevelopment.standalone.serverui.jobs.JobsOverviewModel;
import de.coerdevelopment.standalone.util.TimeCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowJobUI {

    public static JDialog getJobDetailDialog(Job job, Component relativeTo) {
        JDialog dialog = new JDialog();
        dialog.setTitle(job.jobname);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        dialog.setContentPane(new ShowJobUI(job, dialog).contentPanel);
        dialog.pack();
        dialog.setMinimumSize(dialog.getSize());
        dialog.setLocationRelativeTo(relativeTo);
        return dialog;
    }

    public JPanel contentPanel;
    private JPanel headerPanel;
    private JLabel jobnameLabel;
    private JPanel infoPanel;
    private JLabel descriptionLabel;
    private JLabel repeatingLabel;
    private JLabel intervalLabel;
    private JButton startRunButton;
    private JPanel buttonPanel;
    private JButton planRunButton;
    private JScrollPane tableScrollPanel;
    private JPanel footerPanel;
    private JButton closeButton;
    private JTable jobRunsTable;
    private JButton refreshTableButton;
    private JLabel jobRunsLabel;

    private final int maxTableEntries = 50;

    private Job job;
    private JobsOverviewModel tableModel;

    private JDialog frame;

    private ShowJobUI(Job job, JDialog frame) {
        this.job = job;
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        refreshTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setDisplayedRuns(Job.getRecentRuns(job.jobname, maxTableEntries));
                tableModel.fireTableDataChanged();
            }
        });
        startRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(ShowJobUI.this.contentPanel, "Do you really want to start a new run of " + job.jobname + "?",
                        "Start new Run?", JOptionPane.YES_NO_OPTION);
                if (confirmed != 0) {
                    return;
                }
                Jobrun createdRun = new Jobrun(job);
                createdRun.start();
                JOptionPane.showMessageDialog(ShowJobUI.this.contentPanel, "Jobrun has been started!",
                        "Jobrun has been started!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        planRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnterStartTimeUI.createUI(job, ShowJobUI.this.contentPanel).setVisible(true);
            }
        });
        updateUI();
    }

    private void createUIComponents() {
        jobRunsTable = new JTable();
        tableModel = new JobsOverviewModel(Job.getRecentRuns(job.jobname, maxTableEntries));
        jobRunsTable.setModel(tableModel);
    }

    private void updateUI() {
        jobnameLabel.setText(job.jobname);
        descriptionLabel.setText(job.description);
        repeatingLabel.setText("Repeating: " + job.repeating);
        intervalLabel.setText("Interval: " + TimeCalculator.getInstance().convertTime(job.interval));
    }

}
