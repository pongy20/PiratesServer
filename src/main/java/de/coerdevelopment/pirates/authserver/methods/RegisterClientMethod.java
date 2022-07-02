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
 * This method will be called if a new client tries to register a new account
 */
public class RegisterClientMethod extends TcpMethod {

    private AccountRepository accountRepository;

    public RegisterClientMethod() {
        super(PiratesMethod.REGISTER_CLIENT.getMethodId());
        accountRepository = AccountRepository.getInstance();
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        String incomingRawData = incomingPackage.getData();
        if (incomingRawData == null) {
            clientThread.send(new Datapackage(PiratesMethod.AUTH_CLIENT.getMethodId(), "Data not valid"));
            return;
        }
        Gson gson = new Gson();
        String[] incomingData = gson.fromJson(incomingRawData, String[].class);
        if (incomingData.length != 2) {
            clientThread.send(new Datapackage(PiratesMethod.AUTH_CLIENT.getMethodId(), "Data not valid"));
            return;
        }
        String passedMail = incomingData[0];
        String passedPassword = incomingData[1];
        if (accountRepository.doesMailExists(passedMail)) {
            clientThread.send(new Datapackage(PiratesMethod.AUTH_CLIENT.getMethodId(), "Mail already exists"));
            return;
        }
        accountRepository.insertAccount(passedMail, passedPassword);
        Account account = accountRepository.getAccountByMail(passedMail);
        clientThread.accountId = account.accountID;
        long time = System.currentTimeMillis();
        String authKey = AuthKeyGenerator.getInstance().generateAuthKey(account.accountID, time);
        String json = gson.toJson(new Object[]{account.accountID, authKey, time});
        //info client - registration was successful
        clientThread.send(new Datapackage(PiratesMethod.AUTH_CLIENT.getMethodId(), json));
        // send available worlds to client
        SendAvailableWorldsMethod worldsMethod = new SendAvailableWorldsMethod();
        worldsMethod.onMethod(null, clientThread);
    }
}
