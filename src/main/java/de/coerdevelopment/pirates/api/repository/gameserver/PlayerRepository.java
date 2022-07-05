package de.coerdevelopment.pirates.api.repository.gameserver;

import de.coerdevelopment.pirates.api.GameWorld;
import de.coerdevelopment.pirates.api.Player;
import de.coerdevelopment.pirates.api.repository.auth.AccountRepository;
import de.coerdevelopment.pirates.api.repository.auth.GameWorldRepository;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Table syntax:
 *
 * playerId Integer PK
 * accountId Integer UQ
 * username VARCHAR(32) UQ
 * diamonds Integer
 */
public class PlayerRepository extends Repository {

    private static PlayerRepository instance;
    
    public static PlayerRepository getInstance() {
        if (instance == null) {
            instance = new PlayerRepository();
        }
        return instance;
    }
    
    private PlayerRepository() {
        super("player");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    " ( playerId INTEGER AUTO_INCREMENT," +
                    "accountId INTEGER NOT NULL," +
                    "username VARCHAR(32) NOT NULL," +
                    "diamonds INTEGER NOT NULL," +
                    "CONSTRAINT pk_player PRIMARY KEY(playerId)," +
                    "CONSTRAINT uk_acc UNIQUE(accountId)," +
                    "CONSTRAINT uk_username UNIQUE(username))");
            ps.execute();

            PreparedStatement ps2 = sql.getConnection().prepareStatement("ALTER TABLE " + tableName + " AUTO_INCREMENT = 2000000");
            ps2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPlayer(int accountId, String username) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName +
                    " (accountId,username,diamonds) VALUES (?,?,?)");
            ps.setInt(1, accountId);
            ps.setString(2, username);
            ps.setInt(3, 100);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayer(Player player) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName +
                    " SET username = ?, diamonds = ? WHERE playerId = ?");
            ps.setString(1, player.username);
            ps.setInt(2, player.diamonds);
            ps.setInt(3, player.playerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayerByAccountId(int accountId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName +
                    " WHERE accountId = ?");
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getPlayerByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Player getPlayerByUsername(String username) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName +
                    " WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getPlayerByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Player getPlayerByPlayerId(int playerId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName +
                    " WHERE playerId = ?");
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getPlayerByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Player getPlayerByResultSetEntry(ResultSet rs) throws SQLException {
        int accountId = rs.getInt("accountId");
        int playerId = rs.getInt("playerId");
        String username = rs.getString("username");
        int diamonds = rs.getInt("diamonds");
        return new Player(playerId,accountId,username,diamonds);
    }

}
