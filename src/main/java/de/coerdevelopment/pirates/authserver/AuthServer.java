package de.coerdevelopment.pirates.authserver;

import de.coerdevelopment.pirates.authserver.methods.AuthClientMethod;
import de.coerdevelopment.pirates.authserver.methods.RegisterClientMethod;
import de.coerdevelopment.pirates.api.repository.AccountRepository;
import de.coerdevelopment.pirates.api.repository.GameWorldRepository;
import de.coerdevelopment.pirates.api.repository.PlayerRepository;
import de.coerdevelopment.standalone.net.server.LoginServer;
import de.coerdevelopment.standalone.repository.SQL;
import de.coerdevelopment.standalone.util.DebugMessage;

public class AuthServer extends LoginServer {

    AccountRepository accountRepository;
    GameWorldRepository worldRepository;
    PlayerRepository playerRepository;

    public AuthServer(int port) {
        super(port);
        initSQL();

        accountRepository = AccountRepository.getInstance();
        worldRepository = GameWorldRepository.getInstance();
        playerRepository = PlayerRepository.getInstance();

        createTables();

        registerLoginMethods();
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
        worldRepository.createTable();
        playerRepository.createTable();
    }

}
