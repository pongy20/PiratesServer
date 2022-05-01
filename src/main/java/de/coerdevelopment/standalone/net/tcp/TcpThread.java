package de.coerdevelopment.standalone.net.tcp;

import de.coerdevelopment.standalone.json.JsonConverter;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.util.DebugMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public abstract class TcpThread extends Thread {

    public Socket socket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private List<TcpMethod> registeredMethods;

    public TcpThread(Socket socket, List<TcpMethod> registeredMethods) {
        this.socket = socket;
        this.registeredMethods = registeredMethods;
    }

    @Override
    public void run() {
        super.run();
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            while(!isInterrupted()) {
                Object readObj;
                readObj = in.readObject();      // will wait until a message comes in at this time
                if (readObj == null) {
                    DebugMessage.sendErrorMessage("Datapackage with NULL data arrived.");
                    break;
                }
                Object convertedObj = null;
                try {
                    convertedObj = JsonConverter.getInstance().convertJsonToObject(readObj.toString());  // Exception catch in this case makes sense
                } catch (Exception e) {
                    DebugMessage.sendErrorMessage("Cant convert datapackage using JSON.");
                    e.printStackTrace();
                    break;
                }
                if (convertedObj instanceof Datapackage) {
                    Datapackage data = (Datapackage) convertedObj;
                    for (TcpMethod method : registeredMethods) {
                        if (method.getMethodID().equals(data.getMethodID())) {
                            method.onMethod(data, TcpThread.this);
                            break;
                        }
                    }
                } else {
                    DebugMessage.sendErrorMessage("Incoming Data was not an instance of datapackage.");
                    break;
                }
            }
        } catch (SocketException se) {
            if (se.getMessage().equals("Connection reset")) {
                closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            DebugMessage.sendErrorMessage(e.getMessage());
        }
    }

    public void send(Datapackage datapackage) {
        try {
            String json = JsonConverter.getInstance().convertDatapackageToJson(datapackage);
            DebugMessage.sendInfoMessage(json);
            out.writeObject(json);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void closeConnection();

}
