package de.coerdevelopment.standalone.net.tcp;

import de.coerdevelopment.standalone.json.JsonConverter;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.util.DebugMessage;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public abstract class TcpThread extends Thread {

    public int accountId;

    public Socket socket;

    protected InputStream in;
    protected OutputStream out;

    public int timeout;

    protected List<TcpMethod> registeredMethods;

    public TcpThread(Socket socket, List<TcpMethod> registeredMethods) {
        this(socket, registeredMethods, 3000);
    }
    public TcpThread(Socket socket, List<TcpMethod> registeredMethods, int timeout) {
        this.socket = socket;
        this.registeredMethods = registeredMethods;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        super.run();
        try {
            socket.setSoTimeout(timeout);
            socket.setKeepAlive(false);
            out = socket.getOutputStream();
            out.flush();
            in = socket.getInputStream();

            while(!isInterrupted()) {
                byte[] data = new byte[4096];
                int length = in.read(data);
                if (length < 0) {
                    continue;
                }
                out.write(new byte[1]);
                String readString = new String(data, 0, length);
                if (readString == null) {
                    DebugMessage.sendErrorMessage("Datapackage with NULL data arrived.");
                    continue;
                }
                Object convertedObj = null;
                try {
                    convertedObj = JsonConverter.getInstance().convertJsonToObject(readString);  // Exception catch in this case makes sense
                } catch (Exception e) {
                    DebugMessage.sendErrorMessage("Cant convert datapackage using JSON.");
                    continue;
                }
                if (convertedObj instanceof Datapackage) {
                    Datapackage datapackage = (Datapackage) convertedObj;
                    for (TcpMethod method : registeredMethods) {
                        if (method.getMethodID().equals(datapackage.getMethodID())) {
                            method.onMethod(datapackage, TcpThread.this);
                            break;
                        }
                    }
                } else {
                    DebugMessage.sendErrorMessage("Incoming Data was not an instance of datapackage.");
                    break;
                }
            }
        } catch (Exception e) {
            DebugMessage.sendErrorMessage(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void send(Datapackage datapackage) {
        try {
            String json = JsonConverter.getInstance().convertDatapackageToJson(datapackage);
            DebugMessage.sendDebugMessage(json);
            out.write(json.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void closeConnection();

}
