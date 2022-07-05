package de.coerdevelopment.pirates.gameserver;

import de.coerdevelopment.pirates.api.GameWorld;
import de.coerdevelopment.pirates.api.repository.gameserver.*;
import de.coerdevelopment.pirates.gameserver.methods.LoginPlayerMethod;
import de.coerdevelopment.standalone.net.server.GameServer;
import de.coerdevelopment.standalone.repository.SQL;
import de.coerdevelopment.standalone.util.DebugMessage;

public class PiratesGameServer extends GameServer {

    public static PiratesGameServer instance;

    public GameWorld gameWorld;

    public PlayerRepository playerRepository;
    public PlayerLoginRepository playerLoginRepository;
    public PlayerRankRepository playerRankRepository;
    public IslandRepository islandRepository;
    public BuildingRepository buildingRepository;
    public BuildingTypeRepository buildingTypeRepository;

    public PiratesGameServer(int port, GameWorld world) {
        super(port);
        instance = this;
        this.gameWorld = world;
    }

    public PiratesGameServer(GameWorld world) {
        this(DEFAULT_PORT, world);
    }

    @Override
    public void startServer() {
        super.startServer();
        initSQL();

        playerRepository = PlayerRepository.getInstance();
        playerLoginRepository = PlayerLoginRepository.getInstance();
        playerRankRepository = PlayerRankRepository.getInstance();
        islandRepository = IslandRepository.getInstance();
        buildingRepository = BuildingRepository.getInstance();
        buildingTypeRepository = BuildingTypeRepository.getInstance();

        createTables();

        registerMethods();
    }

    public void registerMethods() {
        tcpServer.registerMethod(new LoginPlayerMethod());
    }

    private void initSQL() {
        SQL sql = SQL.newSQL("localhost", "pirates_anson", "pirates", "pirates_anson", "3306");
        if (sql.initSQL()) {
            DebugMessage.sendInfoMessage("SQL-Connection successful!");
        }
    }

    private void createTables() {
        playerRepository.createTable();
        buildingTypeRepository.createTable();
        playerRankRepository.createTable();
        playerLoginRepository.createTable();
        islandRepository.createTable();
        buildingRepository.createTable();
    }
}
