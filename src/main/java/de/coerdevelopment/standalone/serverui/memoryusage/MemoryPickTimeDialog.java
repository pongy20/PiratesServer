package de.coerdevelopment.standalone.serverui.memoryusage;

import com.github.lgooddatepicker.components.DateTimePicker;

import javax.swing.*;
import java.awt.*;

public class MemoryPickTimeDialog {

    public static void main(String[] args) {
        createDialog(null).setVisible(true);
    }

    public static JDialog createDialog(JFrame owner) {
        JDialog dialog = new JDialog(owner);

        dialog.setTitle("Select a Timeframe");
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();

        contentPanel.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Select a start and end time!");
        headerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        JButton submitButton = new JButton("submit");

        DateTimePicker startTimePicker = new DateTimePicker();
        DateTimePicker endTimePicker = new DateTimePicker();

        JLabel startTimeLabel = new JLabel("Start time:");
        startTimeLabel.setLabelFor(startTimeLabel);

        contentPanel.add(headerLabel, BorderLayout.NORTH);
        contentPanel.add(startTimePicker, BorderLayout.WEST);
        contentPanel.add(endTimePicker, BorderLayout.EAST);
        contentPanel.add(submitButton, BorderLayout.SOUTH);

        dialog.add(contentPanel);

        dialog.setMinimumSize(new Dimension(250, 300));

        return dialog;
    }

}
