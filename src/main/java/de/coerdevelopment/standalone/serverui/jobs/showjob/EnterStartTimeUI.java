package de.coerdevelopment.standalone.serverui.jobs.showjob;

import com.github.lgooddatepicker.components.DateTimePicker;
import de.coerdevelopment.standalone.job.Job;
import de.coerdevelopment.standalone.job.Jobrun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;

public class EnterStartTimeUI {

    public static JDialog createUI(Job job, Component relativeTo) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Select Start Time");
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        dialog.setContentPane(new EnterStartTimeUI(job, dialog).contentPanel);
        dialog.pack();
        dialog.setMinimumSize(dialog.getSize());
        dialog.setLocationRelativeTo(relativeTo);
        return dialog;
    }

    public JPanel contentPanel;
    private JPanel footerPanel;
    private JButton startRunButton;
    private JButton cancelButton;
    private JLabel headerLabel;

    private Job job;
    private JDialog dialog;

    private DateTimePicker startTimePicker;

    private EnterStartTimeUI(Job job, JDialog dialog) {
       this.job = job;
       this.dialog = dialog;

        startTimePicker = new DateTimePicker();
        startTimePicker.setDateTimePermissive(LocalDateTime.now().plusMinutes(10));


        contentPanel.add(startTimePicker, BorderLayout.CENTER);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        updateUI();
        startRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date enteredDate = getDateByTimePicker(startTimePicker);
                if (enteredDate.before(new Date(System.currentTimeMillis()))) {
                    JOptionPane.showMessageDialog(EnterStartTimeUI.this.contentPanel, "The start time has to be in future!",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Jobrun jobrun = new Jobrun(job, enteredDate.getTime());
                int confirmed = JOptionPane.showConfirmDialog(EnterStartTimeUI.this.contentPanel, "Do you really want to plan a new run of " + job.jobname + " on " + jobrun.getFormattedStartDateTime() + "?",
                        "Plan new Run?", JOptionPane.YES_NO_OPTION);
                if (confirmed != 0) {
                    return;
                }
                jobrun.startOnStartTime();
                JOptionPane.showMessageDialog(EnterStartTimeUI.this.contentPanel, "Jobrun has been planned on " + jobrun.getFormattedStartDateTime() + "!",
                        "Jobrun has been planned", JOptionPane.INFORMATION_MESSAGE);
                dialog.setVisible(false);
            }
        });
    }

    private void updateUI() {
        headerLabel.setText("Please choose a time to start " + job.jobname);
    }

    private Date getDateByTimePicker(DateTimePicker dateTimePicker) {
        int month = dateTimePicker.getDatePicker().getDate().getMonth().getValue() - 1;
        int year = dateTimePicker.getDatePicker().getDate().getYear() - 1900;
        int day = dateTimePicker.getDatePicker().getDate().getDayOfMonth();
        int hour = dateTimePicker.getTimePicker().getTime().getHour();
        int minutes = dateTimePicker.getTimePicker().getTime().getMinute();
        Date javaDate = new java.util.Date(year, month, day, hour, minutes);

        return javaDate;
    }

}
