package de.coerdevelopment.pirates.authserver.methods;

import com.google.gson.Gson;
import de.coerdevelopment.pirates.api.Account;
import de.coerdevelopment.pirates.api.repository.auth.AccountRepository;
import de.coerdevelopment.pirates.authserver.utils.AuthKeyGenerator;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;

/**
 * Incoming Package has to include mail and hashed password
 * mail and password has to be passed in an array
 */
public class AuthClientMethod extends TcpMethod {

    private AccountRepository accountRepository;

    public AuthClientMethod() {
        super(PiratesMethod.AUTH_CLIENT.getMethodId());
        accountRepository = AccountRepository.getInstance();
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        String incomingRawData = incomingPackage.getData();
        if (incomingRawData == null) {
            clientThread.send(new Datapackage(methodID, "Data not valid"));
            return;
        }
        Gson gson = new Gson();
        String[] incomingData = gson.fromJson(incomingRawData, String[].class);
        if (incomingData.length != 2) {
            clientThread.send(new Datapackage(methodID, "Data not valid"));
            return;
        }
        String passedMail = incomingData[0];
        String passedPassword = incomingData[1];
        boolean passwordCorrect = accountRepository.isPasswordCorrect(passedMail, passedPassword);
        if (!passwordCorrect) {
            clientThread.send(new Datapackage(methodID, "Mail and Password don't match!"));
            return;
        }
        Account account = accountRepository.getAccountByMail(passedMail);
        clientThread.accountId = account.accountID;
        long time = System.currentTimeMillis();
        String authKey = AuthKeyGenerator.getInstance().generateAuthKey(account.accountID, time);
        String json = gson.toJson(new Object[]{account.accountID, authKey, time});
        clientThread.send(new Datapackage(methodID, json));
        SendAvailableWorldsMethod worldsMethod = new SendAvailableWorldsMethod();
        worldsMethod.onMethod(null, clientThread);
    }
}
