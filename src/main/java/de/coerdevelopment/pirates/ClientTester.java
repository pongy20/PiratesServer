package de.coerdevelopment.pirates;

import de.coerdevelopment.standalone.net.tcp.TcpClient;

import java.net.Socket;

public class ClientTester {

    public static void main(String[] args)  {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("localhost", 42201);
                    //client.readIncomming();
                    System.out.println("Test");
                    System.out.println(socket.isConnected());
                    socket.getOutputStream().write(new byte[1]);
                    //Thread.sleep(1000);
                    socket.close();
                    System.out.println("Bin ich dumm?: " + (!socket.isClosed() ? "ja" : "nein"));
                } catch (Exception e) {
                    e.printStackTrace();;
                }
            }
        });
        thread.start();

    }

}
