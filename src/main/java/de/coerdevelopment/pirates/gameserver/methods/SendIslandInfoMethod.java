package de.coerdevelopment.pirates.gameserver.methods;

import de.coerdevelopment.pirates.api.GameWorld;
import de.coerdevelopment.pirates.api.Island;
import de.coerdevelopment.pirates.api.Player;
import de.coerdevelopment.pirates.api.building.instances.Farm;
import de.coerdevelopment.pirates.api.building.instances.Lumberjack;
import de.coerdevelopment.pirates.api.building.instances.Mine;
import de.coerdevelopment.pirates.api.building.instances.Storage;
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
        Player player = new Player(100000, 10000, "pongy20", Integer.MAX_VALUE);
        Island island = new Island(10000, player, "Coole Insel", Integer.MAX_VALUE, new Lumberjack(1), new Mine(1), new Farm(1), new Storage(1));
        Datapackage datapackage = new Datapackage(methodID, island);
        clientThread.send(datapackage);
    }
}
