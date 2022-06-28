package de.coerdevelopment.pirates.gameserver.methods;

import de.coerdevelopment.pirates.api.GameWorld;
import de.coerdevelopment.pirates.api.Island;
import de.coerdevelopment.pirates.api.Player;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;

public class SendIslandInfoMethod extends TcpMethod {

    public SendIslandInfoMethod() {
        super(PiratesMethod.SEND_ISLAND_INFO.getMethodId());
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        Player player = new Player(100000, 10000, new GameWorld("Anson", 42210, "localhost"), "pongy20", Integer.MAX_VALUE);
        Island island = new Island(10000, player, "Coole Insel", 1000, 1000, 2000, Integer.MAX_VALUE, Island.getBasicBuildings());
        Datapackage datapackage = new Datapackage(methodID, island);
        clientThread.send(datapackage);
    }
}
