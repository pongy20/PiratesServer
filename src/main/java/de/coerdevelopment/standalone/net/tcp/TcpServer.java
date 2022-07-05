package de.coerdevelopment.standalone.net.tcp;

import de.coerdevelopment.standalone.util.DebugMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpServer extends Thread {

    /**
     * The name of the server wich will be displayed in console
     * Eg. 'Loginserver','German#1GameServer'
     */
    public final String servername;
    public final int port;

    protected ServerSocket serverSocket;
    protected List<TcpClientThread> clients;
    protected boolean running;

    protected List<TcpMethod> registeredMethods;

    public TcpServer(int port, String servername) {
        this.port = port;
        this.servername = "[" + servername + "]";
        running = false;
        clients = new ArrayList<>();
        registeredMethods = new ArrayList<>();
    }

    @Override
    public void run() {
        if (running) {
            return;
        }
        running = true;
        try {
            serverSocket = new ServerSocket(port);
            DebugMessage.sendInfoMessage("Server has been started...");
            DebugMessage.sendInfoMessage("Waiting for new clients...");
            while (!isInterrupted()) {
                Socket client;
                client = serverSocket.accept();
                addClient(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DebugMessage.sendErrorMessage(e.getMessage());
        }
    }

    /**
     * Method will be called after a new client-connection have been accepted
     */
    public void addClient(Socket client) {
        DebugMessage.sendInfoMessage("Client connected!");
        TcpClientThread thread = new TcpClientThread(this, client);
        clients.add(thread);
        thread.start();
    }

    /**
     * Method will be called after a client disconnects from server
     */
    public void removeClient(TcpClientThread thread) {
        clients.remove(thread);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        running = false;
    }
    public List<TcpClientThread> getClients() {
        return clients;
    }
    public void registerMethod(TcpMethod method) {
        registeredMethods.add(method);
    }

    public List<TcpMethod> getRegisteredMethods() {
        return registeredMethods;
    }

}
