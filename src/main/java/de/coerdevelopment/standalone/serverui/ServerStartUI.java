package de.coerdevelopment.standalone.serverui;

import de.coerdevelopment.pirates.gameserver.PiratesGameServer;
import de.coerdevelopment.pirates.authserver.AuthServer;
import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.net.server.ServerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ServerStartUI {

    private static ServerStartUI instance;
    private static JFrame frameInstance;

    public static ServerStartUI getInstance() {
        return instance;
    }

    public static ServerStartUI createInstance(Map<String, Object[]> servers) {
        instance = new ServerStartUI(servers);
        return instance;
    }

    public JFrame createUI(Map<String, Object[]> servers) {
        assert instance != null;
        JFrame frame = new JFrame("WizzServer - Start");
        frame.setContentPane(instance.contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300, 300));
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frameInstance = frame;
        return frame;
    }

    private JPanel contentPanel;
    private JPanel headerPanel;
    private JLabel headerLabel;
    private JLabel creatorLabel;
    private JPanel centerPanel;
    private JPanel footerPanel;
    private JLabel serverTypeLabel;
    private JComboBox serverTypeCombo;
    private JLabel portLabel;
    private JFormattedTextField portField;
    private JButton startServerButton;
    private JButton cancelButton;
    private JButton createWorldButton;
    private JButton changePortButton;

    private Map<String, Object[]> servers;

    private ServerStartUI(Map<String, Object[]> servers) {
        this.servers = servers;
        changePortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                portField.setEditable(!portField.isEditable());
                if (portField.isEditable()) {
                    changePortButton.setText("save");
                } else {
                    changePortButton.setText("change");
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameInstance.dispose();
            }
        });
        serverTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePortField();
            }
        });
        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        updateList();
        updatePortField();
    }

    private void updateList() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (String servername : servers.keySet()) {
            model.addElement(servername);
        }
        serverTypeCombo.setModel(model);
    }

    private void updatePortField() {
        String selectedItem = serverTypeCombo.getSelectedItem().toString();
        if (selectedItem == null) {
            return;
        }
        int port = 0;
        for (String servername : servers.keySet()) {
            if (selectedItem.equals(servername)) {
                port = (int) servers.get(servername)[1];
            }
        }
        portField.setText(port + "");
    }

    private boolean validateInput() {
        if (portField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frameInstance, "Please enter a port!",
                    "Invalid Port!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int port = -1;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frameInstance, "Please enter a valid port!",
                    "Invalid Port!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (port < 2 || port > 65535) {
            JOptionPane.showMessageDialog(frameInstance, "Please enter a valid port!" +
                            "\nValid port numbers: 2 - 65535",
                    "Invalid Port!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void startServer() {
        if (!validateInput()) {
            return;
        }
        Server server = null;

        String selectedItem = serverTypeCombo.getSelectedItem().toString();
        Object[] info = servers.get(selectedItem);
        if (info == null) {
            return;
        }

        if (((ServerType) info[0]).equals(ServerType.LOGINSERVER)) {
            server = new AuthServer((int) info[1]);
        } else {
            server = new PiratesGameServer((int) info[1]);
        }

        OverviewUI ui = OverviewUI.createInstance(server);
        JFrame frame = ui.createUI();
        frame.setVisible(true);

        frameInstance.setVisible(false);
    }

}
