package de.coerdevelopment.pirates.authserver.methods;

import com.google.gson.Gson;
import de.coerdevelopment.pirates.api.Account;
import de.coerdevelopment.pirates.authserver.repository.AccountRepository;
import de.coerdevelopment.pirates.authserver.utils.AuthKeyGenerator;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;
import de.coerdevelopment.standalone.util.ErrorMessage;

/**
 * This method will be called if a new client tries to register a new account
 */
public class RegisterClientMethod extends TcpMethod {

    private AccountRepository accountRepository;

    public RegisterClientMethod() {
        super("REGISTER_CLIENT");
        accountRepository = AccountRepository.getInstance();
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        String incomingRawData = incomingPackage.getData();
        if (incomingRawData == null) {
            clientThread.send(new Datapackage(ErrorMessage.DATAPACKAGE_DATA_NULL));
            return;
        }
        Gson gson = new Gson();
        String[] incomingData = gson.fromJson(incomingRawData, String[].class);
        if (incomingData.length != 2) {
            clientThread.send(new Datapackage(ErrorMessage.DATAPACKAGE_DATA_NULL));
            return;
        }
        String passedMail = incomingData[0];
        String passedPassword = incomingData[1];
        if (accountRepository.doesMailExists(passedMail)) {
            clientThread.send(new Datapackage(ErrorMessage.MAIL_ALREADY_EXISTS));
            return;
        }
        accountRepository.insertAccount(passedMail, passedPassword);
        Account account = accountRepository.getAccountByMail(passedMail);
        long time = System.currentTimeMillis();
        String authKey = AuthKeyGenerator.getInstance().generateAuthKey(account.accountID, time);
        String json = gson.toJson(new Object[]{account.accountID, authKey});
        clientThread.send(new Datapackage("REGISTER_SUCCESS", json));
    }
}