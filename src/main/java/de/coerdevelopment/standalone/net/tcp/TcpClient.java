package de.coerdevelopment.standalone.net.tcp;

import de.coerdevelopment.standalone.json.JsonConverter;
import de.coerdevelopment.standalone.net.Datapackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TcpClient extends Socket {

    private Thread listiningThread;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public TcpClient(String host, int port) throws IOException {
        super(host, port);

        in = new ObjectInputStream(getInputStream());
        out = new ObjectOutputStream(getOutputStream());
    }

    public void readIncomming() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Object o = in.readObject();
                        Datapackage receivedPck = (Datapackage) JsonConverter.getInstance().convertJsonToObject((String) o);
                        System.out.println(receivedPck.getData());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void send(Datapackage datapackage) {
        try {
            out.writeObject(JsonConverter.getInstance().convertDatapackageToJson(datapackage));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
