package de.coerdevelopment.pirates.api.repository;

import de.coerdevelopment.pirates.api.GameWorld;
import de.coerdevelopment.pirates.api.Player;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    " ( playerId INTEGER auto_increment," +
                    "accountId Integer NOT NULL," +
                    "worldId Integer NOT NULL," +
                    "username VARCHAR(32) NOT NULL," +
                    "diamonds INTEGER NOT NULL," +
                    "CONSTRAINT pk_player PRIMARY KEY(playerId)," +
                    "CONSTRAINT uk_acc_wl UNIQUE(accountId,worldId)," +
                    "CONSTRAINT fk_acc FOREIGN KEY(accountId) REFERENCES " + AccountRepository.getInstance().tableName + "(accountId) ON DELETE CASCADE," +
                    "CONSTRAINT fk_world FOREIGN KEY(worldId) REFERENCES " + GameWorldRepository.getInstance().tableName + "(worldId) ON DELETE CASCADE," +
                    "CONSTRAINT uk_username UNIQUE(username,worldId))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(int accountId, int worldId, String username) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName +
                    " (accountId,worldId,username,diamonds) VALUES (?,?,?,?)");
            ps.setInt(1, accountId);
            ps.setInt(2, worldId);
            ps.setString(3, username);
            ps.setInt(4, 100);
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

    public Player getPlayer(int accountId, int worldId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName +
                    " WHERE accountId = ? AND worldId = ?");
            ps.setInt(1, accountId);
            ps.setInt(2, worldId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getPlayerByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Player getPlayer(int worldId, String username) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName +
                    " WHERE worldId = ? AND username = ?");
            ps.setInt(1, worldId);
            ps.setString(2, username);
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
        int worldId = rs.getInt("worldId");
        String username = rs.getString("username");
        int diamonds = rs.getInt("diamonds");
        GameWorld world = GameWorldRepository.getInstance().getWorld(rs.getInt("worldId"));
        return new Player(playerId,accountId,world,username,diamonds);
    }

}
