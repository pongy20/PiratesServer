package de.coerdevelopment.pirates.api.gameserver.methods;

import com.google.gson.Gson;
import de.coerdevelopment.pirates.api.gameserver.GameServerTcpThread;
import de.coerdevelopment.pirates.authserver.utils.AuthKeyGenerator;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;

/**
 * data: [accountId, authKey, millis]
 */
public class LoginPlayerMethod extends GameServerTcpMethod {

    public LoginPlayerMethod() {
        super(PiratesMethod.LOGIN_PLAYER.getMethodId());
    }

    @Override
    public void onMethod(Datapackage incomingPackage, GameServerTcpThread clientThread) {
        if (incomingPackage.getData() == null) {
            return;
        }
        Gson gson = new Gson();
        Object[] rawData = gson.fromJson(incomingPackage.getData(), Object[].class);
        if (rawData.length != 3) {
            return;
        }
        int accountId = (int) rawData[0];
        String authKey = String.valueOf(rawData[1]);
        long millis = (long) rawData[2];
        boolean correct = AuthKeyGenerator.getInstance().isAuthKeyCorrect(accountId, millis, authKey);
        clientThread.isAuthorized = correct;
        clientThread.send(new Datapackage("LOGIN_PLAYER_FEEDBACK", correct));
        if (correct) {
            //TODO: get / create island
        }
    }
}
