package de.coerdevelopment.standalone.serverui.playermanagement;

import de.coerdevelopment.standalone.net.server.Server;
import de.coerdevelopment.standalone.net.tcp.TcpClientThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel {
    public JPanel contentPanel;
    private JPanel headerPanel;
    private JLabel headerLabel;
    private JLabel clientsConnectedLabel;
    private JButton refreshButton;
    private JList playersList;
    private JScrollPane centerScrollPanel;

    private Server server;

    public PlayerPanel(Server server) {
        this.server = server;
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateComponents();
            }
        });
        updateComponents();
    }

    public void updateComponents() {
        int clientsCount = server.tcpServer.getClients().size();
        int maxClients = server.maxOnlinePlayers;
        clientsConnectedLabel.setText("Clients connected: " + clientsCount + "/" + maxClients);

        DefaultListModel playersListModel = new DefaultListModel();
        for (TcpClientThread thread : server.tcpServer.getClients()) {
            playersListModel.addElement(thread.socket.getInetAddress().getHostAddress() + ":" + thread.socket.getPort());
        }

        playersList.setModel(playersListModel);
    }
}
