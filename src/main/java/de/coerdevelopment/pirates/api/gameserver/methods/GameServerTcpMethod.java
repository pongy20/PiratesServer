package de.coerdevelopment.pirates.api.gameserver.methods;

import de.coerdevelopment.pirates.api.gameserver.GameServerTcpThread;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;

public abstract class GameServerTcpMethod extends TcpMethod {
    public GameServerTcpMethod(String methodID) {
        super(methodID);
    }

    public abstract void onMethod(Datapackage incomingPackage, GameServerTcpThread clientThread);

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        GameServerTcpThread gameServerTcpThread = (GameServerTcpThread) clientThread;
        onMethod(incomingPackage, gameServerTcpThread);
    }
}
