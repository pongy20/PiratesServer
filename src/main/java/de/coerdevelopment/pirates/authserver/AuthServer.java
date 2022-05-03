package de.coerdevelopment.pirates.authserver;

import de.coerdevelopment.pirates.authserver.methods.AuthClientMethod;
import de.coerdevelopment.pirates.authserver.methods.RegisterClientMethod;
import de.coerdevelopment.pirates.authserver.repository.AccountRepository;
import de.coerdevelopment.standalone.net.server.LoginServer;
import de.coerdevelopment.standalone.repository.SQL;
import de.coerdevelopment.standalone.util.DebugMessage;

public class AuthServer extends LoginServer {

    AccountRepository accountRepository;

    public AuthServer(int port) {
        super(port);
        initSQL();

        accountRepository = AccountRepository.getInstance();

        createTables();
    }

    @Override
    public void registerLoginMethods() {
        super.registerLoginMethods();
        tcpServer.registerMethod(new AuthClientMethod());
        tcpServer.registerMethod(new RegisterClientMethod());
    }

    public AuthServer() {
        this(DEFAULT_PORT);
    }

    public void initSQL() {
        SQL sql = SQL.newSQL("localhost", "pirates", "pirates", "pirates", "3306");
        if (sql.initSQL()) {
            DebugMessage.sendInfoMessage("SQL-Connection successful!");
        }
    }
    public void createTables() {
        accountRepository.createTable();
    }

}
