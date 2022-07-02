package de.coerdevelopment.pirates.authserver.methods;

import com.google.gson.Gson;
import de.coerdevelopment.pirates.api.GameWorld;
import de.coerdevelopment.pirates.api.repository.auth.GameWorldRepository;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;

import java.util.List;
import java.util.Map;

public class SendAvailableWorldsMethod extends TcpMethod {

    public SendAvailableWorldsMethod() {
        super(PiratesMethod.SEND_AVAILABLE_WORLDS.getMethodId());
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        List<GameWorld> worldsPlayedOn = GameWorldRepository.getInstance().getAllWorldsByAccount(clientThread.accountId);
        List<GameWorld> availableWorlds = GameWorldRepository.getInstance().getAllEnabledWorlds();

        Gson gson = new Gson();

        String[] toSend = new String[2];
        toSend[0] = gson.toJson(availableWorlds);
        toSend[1] = gson.toJson(worldsPlayedOn);

        Datapackage datapackage = new Datapackage(methodID, toSend);
        clientThread.send(datapackage);
    }
}
