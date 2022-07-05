package de.coerdevelopment.standalone.serverui;

import de.coerdevelopment.pirates.PiratesServerStart;
import de.coerdevelopment.pirates.api.GameWorld;
import de.coerdevelopment.pirates.api.repository.auth.GameWorldRepository;
import de.coerdevelopment.pirates.gameserver.PiratesGameServer;
import de.coerdevelopment.pirates.authserver.AuthServer;
import de.coerdevelopment.standalone.net.server.LoginServer;
import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.net.server.ServerType;
import de.coerdevelopment.standalone.repository.SQL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerStartUI {

    private static ServerStartUI instance;
    private static JFrame frameInstance;

    public static ServerStartUI getInstance() {
        return instance;
    }

    public static ServerStartUI createInstance(LoginServer loginServer) {
        instance = new ServerStartUI(loginServer);
        return instance;
    }

    public JFrame createUI() {
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

    private LoginServer loginServer;

    private List<PiratesGameServer> gameServers;

    private ServerStartUI(LoginServer server) {
        this.loginServer = server;
        this.gameServers = new ArrayList<>();

        if (!SQL.getSQL().isConnected()) {
            JOptionPane.showMessageDialog(frameInstance, "No SQL-Connection - please restart!", "No SQL-Connection", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<GameWorld> worlds = new GameWorldRepository().getAllWorlds();
        for (GameWorld world : worlds) {
            gameServers.add(new PiratesGameServer(world.port, world));
        }

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
        model.addElement("Auth-Server");
        for (PiratesGameServer server : gameServers) {
            model.addElement(server.gameWorld.worldname);
        }
        serverTypeCombo.setModel(model);
    }

    private void updatePortField() {
        String selectedItem = serverTypeCombo.getSelectedItem().toString();
        if (selectedItem == null) {
            return;
        }
        int port = 0;
        if (selectedItem.equals("Auth-Server")) {
            port = loginServer.tcpServer.port;
        } else {
            for (PiratesGameServer server : gameServers) {
                if (server instanceof PiratesGameServer) {
                    if (selectedItem.equals(server.gameWorld.worldname)) {
                        port = server.tcpServer.port;
                    }
                }
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
        Server startedServer = null;

        String selectedItem = serverTypeCombo.getSelectedItem().toString();

        if (selectedItem.equals("Auth-Server")) {
            startedServer = loginServer;
            loginServer.startServer();
        } else {
            for (PiratesGameServer server : gameServers) {
                if (selectedItem.equals(server.gameWorld.worldname)) {
                    startedServer = server;
                    server.startServer();
                }
            }
        }

        Server.instance = startedServer;

        OverviewUI ui = OverviewUI.createInstance(startedServer);
        JFrame frame = ui.createUI();
        frame.setVisible(true);

        frameInstance.setVisible(false);
    }

}
