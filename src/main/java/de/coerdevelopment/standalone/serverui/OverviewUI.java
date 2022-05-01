package de.coerdevelopment.standalone.serverui;

import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.serverui.jobs.JobsOverview;
import de.coerdevelopment.standalone.serverui.playermanagement.PlayerPanel;
import de.coerdevelopment.standalone.util.ConsoleMessage;
import de.coerdevelopment.standalone.util.ServerPreferences;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class OverviewUI {

    private static OverviewUI instance;

    public static OverviewUI getInstance() {
        return instance;
    }

    public static OverviewUI createInstance(Server server) {
        instance = new OverviewUI(server);
        return instance;
    }

    public JFrame frame;

    public JFrame createUI() {
        if (frame != null) {
            return frame;
        }
        JFrame frame = new JFrame("WizzServer - Overview");
        frame.setContentPane(instance.contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 550));      // depends on user screen size, shouldn't ne hardcoded
        frame.setLocationRelativeTo(null);
        frame.pack();
        this.frame = frame;
        return frame;
    }

    private static final long MEGABYTES = 1024L * 1024L;

    public JPanel contentPanel;
    public JTextArea consoleText;
    private JScrollPane consoleScrollPanel;
    private JButton refreshButton;
    private JPanel namePanel;
    private JLabel mainNameLabel;
    private JLabel servernameLabel;
    private JPanel headerPanel;
    private JPanel serverInfoPanel;
    private JLabel statusLabel;
    private JLabel clientsConnectedLabel;
    private JTabbedPane centerPanel;
    private JPanel jobsPanel;
    private JPanel playerPanel;
    private JPanel consolePanel;
    private JPanel consoleInputPanel;
    private JTextField consoleInputField;
    private JButton consoleSendButton;
    private JPanel consoleSettingsPanel;
    private JCheckBox settingsJobMessages;
    private JCheckBox settingsErrorMessages;
    private JCheckBox settingsDebugMessages;
    private JCheckBox settingsInfoMessages;
    private JCheckBox settingsWarningMessages;
    private JButton refreshConsoleButton;
    private JPanel headerButtonPanel;
    private JButton closeServerButton;
    private JLabel memoryUsageLabel;
    private JPanel performancePanel;

    private Server server;
    private Runtime runtime;
    private ServerPerformancePanel serverPerformancePanel;
    private PlayerPanel playerPanelInstance;
    private JobsOverview jobsOverviewInstance;

    private OverviewUI(Server server) {
        this.server = server;
        runtime = Runtime.getRuntime();
        runtime.gc();

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUI();
            }
        });
        refreshConsoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateConsole();
            }
        });

        server.startServer();
        initConsoleMessageSelectedTypes();
        updateUI();
    }

    public void updateUI() {
        servernameLabel.setText(server.tcpServer.servername);
        updateConsole();
        updateStatus();
        serverPerformancePanel.updateComponents();

        jobsOverviewInstance.updateTable();

    }

    public void updateConsole() {
        consoleText.setText("");       // clear actual text

        List<ConsoleMessage.ConsoleMessageType> selectedTypes = getSelectedMessageTypes();
        if (selectedTypes.isEmpty()) return;    // no message type is selected --> nothing to show in this case

        // get all messages matching the selected criteria
        // messages should also be sorted at this time
        SortedSet<ConsoleMessage> messagesByType = server.getFilteredConsoleMessages(selectedTypes);

        for (ConsoleMessage msg : messagesByType) {
            consoleText.setText(consoleText.getText() + msg.toConsoleMessage() + "\n");
        }
        // saves the current selected message types to server preferences
        ServerPreferences.getInstance().updateConsoleMessagePreferences(settingsErrorMessages.isSelected(),
                settingsWarningMessages.isSelected(),
                settingsDebugMessages.isSelected(),
                settingsInfoMessages.isSelected(),
                settingsJobMessages.isSelected());
        ServerPreferences.getInstance().savePreferences();
    }

    public void updateStatus() {
        String status = "offline";
        String statusColorString = "red";
        int clients = 0;
        runtime.gc();
        int usedMemory = (int) bytesToMegabytes(runtime.totalMemory() - runtime.freeMemory());
        int maxMemory = (int) bytesToMegabytes(runtime.maxMemory());
        float usedPercenatge = ((float) Math.round((((float) usedMemory) / ((float) maxMemory)) * 1000)) / 10;
        String memoryUsage = usedMemory + "MB / " + maxMemory + "MB (" + (usedPercenatge) + "%)";
        String memoryUsageColor = "red";

        if (server != null && server.tcpServer != null) {
            if (server.tcpServer.isAlive()) {
                status = "online";
                statusColorString = "green";
            }
            if (server.tcpServer.getClients() != null) {
                clients = server.tcpServer.getClients().size();
            }
            if (usedPercenatge > 0.9) {
                memoryUsageColor = "red";
            } else if (usedPercenatge > 0.75) {
                memoryUsageColor = "orange";
            } else if (usedPercenatge > 0.5) {
                memoryUsageColor = "yellow";
            } else {
                memoryUsageColor = "green";
            }
        }


        memoryUsageLabel.setText("<html>Memory: <font color='" + memoryUsageColor + "'>" + memoryUsage + "</font></html>");
        statusLabel.setText("<html>Status: <font color='" + statusColorString + "'>" + status + "</font></html>");
        clientsConnectedLabel.setText("Client(s) connected: " + clients);
    }

    private List<ConsoleMessage.ConsoleMessageType> getSelectedMessageTypes() {
        List<ConsoleMessage.ConsoleMessageType> selectedTypes = new ArrayList<>();
        if (settingsErrorMessages.isSelected()) selectedTypes.add(ConsoleMessage.ConsoleMessageType.ERROR);
        if (settingsDebugMessages.isSelected()) selectedTypes.add(ConsoleMessage.ConsoleMessageType.DEBUG);
        if (settingsInfoMessages.isSelected()) selectedTypes.add(ConsoleMessage.ConsoleMessageType.INFO);
        if (settingsJobMessages.isSelected()) selectedTypes.add(ConsoleMessage.ConsoleMessageType.JOB);
        if (settingsWarningMessages.isSelected()) selectedTypes.add(ConsoleMessage.ConsoleMessageType.WARNING);
        return selectedTypes;
    }

    private void initConsoleMessageSelectedTypes() {
        ServerPreferences preferences = ServerPreferences.getInstance();
        preferences.loadSettings();
        settingsErrorMessages.setSelected(preferences.sendErrorMessage);
        settingsWarningMessages.setSelected(preferences.sendWarningMessage);
        settingsInfoMessages.setSelected(preferences.sendInfoMessage);
        settingsDebugMessages.setSelected(preferences.sendDebugMessage);
        settingsJobMessages.setSelected(preferences.sendJobMessage);
    }

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTES;
    }

    private void createUIComponents() {
        serverPerformancePanel = new ServerPerformancePanel(server);
        performancePanel = serverPerformancePanel.contentPanel;
        playerPanelInstance = new PlayerPanel(server);
        playerPanel = playerPanelInstance.contentPanel;

        jobsOverviewInstance = new JobsOverview(server);
        jobsPanel = jobsOverviewInstance.contentPanel;
    }

}
