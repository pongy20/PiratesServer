package de.coerdevelopment.pirates;

import com.google.gson.Gson;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
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
                    Gson gson = new Gson();
                    Object[] objects = new Object[]{""};
                    String json = gson.toJson(new Datapackage(PiratesMethod.LOGIN_PLAYER.toString(), new Object[]{"a@b.de", "bla"}));
                    socket.getOutputStream().write(json.getBytes());
                    //Thread.sleep(1000);
                    //socket.close();
                    byte[] data = new byte[4096];
                    int length = socket.getInputStream().read(data);
                    System.out.println(data[0]);
                    System.out.println("Bin ich dumm?: " + (!socket.isClosed() ? "ja" : "nein"));
                } catch (Exception e) {
                    e.printStackTrace();;
                }
            }
        });
        thread.start();

    }

}
