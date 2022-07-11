package de.coerdevelopment.pirates.gameserver.methods;

import de.coerdevelopment.pirates.api.building.BuildingUpgrade;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;

import java.util.List;

public class SendUpgradeInfoMethod extends TcpMethod {

    public SendUpgradeInfoMethod() {
        super(PiratesMethod.SEND_UPGRADE_INFO.getMethodId());
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        BuildingUpgrade upgrade = BuildingUpgrade.getInstance();
        clientThread.send(new Datapackage(methodID, upgrade));
    }
}
