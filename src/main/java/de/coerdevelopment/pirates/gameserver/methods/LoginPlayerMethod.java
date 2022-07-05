package de.coerdevelopment.pirates.gameserver.methods;

import com.google.gson.Gson;
import de.coerdevelopment.pirates.authserver.utils.AuthKeyGenerator;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;
import de.coerdevelopment.standalone.util.DebugMessage;

/**
 * data: [accountId, authKey, millis]
 */
public class LoginPlayerMethod extends TcpMethod {

    public LoginPlayerMethod() {
        super(PiratesMethod.LOGIN_PLAYER.getMethodId());
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        if (incomingPackage.getData() == null) {
            return;
        }
        Gson gson = new Gson();
        String[] rawData = gson.fromJson(incomingPackage.getData(), String[].class);
        if (rawData.length != 3) {
            return;
        }
        int accountId = 0;
        String authKey = "";
        long millis = 0;
        try {
            accountId = Integer.parseInt(rawData[0]);
            authKey = rawData[1];
            millis = Long.parseLong(rawData[2]);
        } catch (ClassCastException e) {
            DebugMessage.sendWarningMessage("Player tried to connect with unusual account data.");
        }
        boolean correct = AuthKeyGenerator.getInstance().isAuthKeyCorrect(accountId, millis, authKey);
        clientThread.accountId = accountId;
        clientThread.isAuthorized = correct;
        clientThread.send(new Datapackage(methodID, correct));
        if (correct) {
            //TODO: get / create island
            SendIslandInfoMethod method = new SendIslandInfoMethod();
            method.onMethod(null, clientThread);
        }
    }
}
