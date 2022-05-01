package de.coerdevelopment.standalone.serverui;

import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.serverui.memoryusage.MemoryGraphFrame;
import de.coerdevelopment.standalone.util.TimeCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerPerformancePanel {
    public JPanel contentPanel;
    private JLabel headerLabel;
    private JLabel runtimeLabel;
    private JTextField runtimeField;
    private JLabel maxMemoryLabel;
    private JTextField maxMemoryField;
    private JLabel usedMemoryLabel;
    private JTextField usedMemoryField;
    private JLabel availableProcessorLabel;
    private JLabel onlineStatusLabel;
    private JTextField onlineStatusField;
    private JTextField availableProcessorField;
    private JButton refreshButton;
    private JButton memoryGraphButton;
    private JLabel startedOnLabel;
    private JTextField startedOnField;
    private JLabel usedThreadsLabel;
    private JTextField usedThreadsField;

    private Server server;

    public ServerPerformancePanel(Server server) {
        this.server = server;
        memoryGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame memoryGraphFrame = new MemoryGraphFrame(server.memoryUsage).createMemoryGraphFrame();
                if (memoryGraphFrame == null) {
                    JOptionPane.showMessageDialog(memoryGraphButton, "Can't create memory graph dialog.");
                    return;
                }
                memoryGraphFrame.setVisible(true);
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateComponents();
            }
        });
        updateComponents();
    }

    public void updateComponents() {
        long serverStart = server.serverStartedTimeStamp;
        runtimeField.setText(TimeCalculator.getInstance().convertTime(System.currentTimeMillis() - serverStart));

        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("dd.MM.YYYY hh:mm");
        Date date = new Date(serverStart);
        startedOnField.setText(formatter.format(date));

        String status = "offline";
        if (server.tcpServer.isAlive()) {
            status = "online";
        }
        onlineStatusField.setText(status);
        onlineStatusField.setDisabledTextColor(status == "online" ? Color.GREEN : Color.RED);

        int maxMemory = (int) OverviewUI.bytesToMegabytes(Runtime.getRuntime().maxMemory());
        int usedMemory = (int) OverviewUI.bytesToMegabytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        int threads = Thread.activeCount();

        maxMemoryField.setText(maxMemory + " MB");
        usedMemoryField.setText(usedMemory + " MB");
        availableProcessorField.setText(availableProcessors + "");
        usedThreadsField.setText(threads + " Threads");
    }

}
