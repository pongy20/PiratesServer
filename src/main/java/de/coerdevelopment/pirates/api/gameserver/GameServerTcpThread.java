package de.coerdevelopment.pirates.api.gameserver;

import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.json.JsonConverter;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpClientThread;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpServer;
import de.coerdevelopment.standalone.net.tcp.TcpThread;
import de.coerdevelopment.standalone.util.DebugMessage;

import java.net.Socket;
import java.net.SocketException;

public class GameServerTcpThread extends TcpClientThread {

    public boolean isAuthorized;

    public GameServerTcpThread(TcpServer server, Socket client) {
        super(server, client);
    }

    @Override
    public void run() {
        super.run();
        try {
            out = socket.getOutputStream();
            out.flush();
            in = socket.getInputStream();

            while(!isInterrupted()) {
                byte[] data = new byte[4096];
                int length = in.read(data);
                if (length < 0) {
                    continue;
                }
                String readString = new String(data, 0, length);
                if (readString == null) {
                    DebugMessage.sendErrorMessage("Datapackage with NULL data arrived.");
                    break;
                }
                Object convertedObj = null;
                try {
                    convertedObj = JsonConverter.getInstance().convertJsonToObject(readString);  // Exception catch in this case makes sense
                } catch (Exception e) {
                    DebugMessage.sendErrorMessage("Cant convert datapackage using JSON.");
                    e.printStackTrace();
                    break;
                }
                if (convertedObj instanceof Datapackage) {
                    Datapackage datapackage = (Datapackage) convertedObj;
                    if (!datapackage.getMethodID().equals(PiratesMethod.LOGIN_PLAYER.getMethodId())) {
                        if (!isAuthorized) {        // client is not authorized
                            send(new Datapackage(PiratesMethod.NOT_AUTHORIZED.getMethodId()));
                            return;
                        }
                    }
                    for (TcpMethod method : registeredMethods) {
                        if (method.getMethodID().equals(datapackage.getMethodID())) {
                            method.onMethod(datapackage, GameServerTcpThread.this);
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

}
