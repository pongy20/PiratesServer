package de.coerdevelopment.pirates.api.repository.auth;

import de.coerdevelopment.pirates.api.repository.auth.AccountRepository;
import de.coerdevelopment.pirates.api.repository.auth.GameWorldRepository;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountPlayerRepository extends Repository {

    private static AccountPlayerRepository instance;

    public static AccountPlayerRepository getInstance() {
        if (instance == null) {
            instance = new AccountPlayerRepository();
        }
        return instance;
    }

    private AccountPlayerRepository() {
        super("acc_pl");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName
                    + "(accountId INTEGER NOT NULL," +
                    "worldId INTEGER NOT NULL," +
                    "CONSTRAINT pk_acc_pl PRIMARY KEY(accountId,worldId)," +
                    "CONSTRAINT fk_accId FOREIGN KEY(accountId) REFERENCES " + AccountRepository.getInstance().tableName + "(accountId) ON DELETE CASCADE," +
                    "CONSTRAINT fk_worldId FOREIGN KEY(worldId) REFERENCES " + GameWorldRepository.getInstance().tableName + "(worldId) ON DELETE CASCADE)");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertEntry(int accountId, int worldId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName +
                    " (accountId,worldId) VALUES (?,?)");
            ps.setInt(1, accountId);
            ps.setInt(2, worldId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
