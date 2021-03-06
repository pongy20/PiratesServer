package de.coerdevelopment.pirates.api.repository.auth;

import de.coerdevelopment.pirates.api.GameWorld;
import de.coerdevelopment.pirates.api.repository.gameserver.PlayerRepository;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameWorldRepository extends Repository {

    private static GameWorldRepository instance;

    public static GameWorldRepository getInstance() {
        if (instance == null) {
            instance = new GameWorldRepository();
        }
        return instance;
    }

    @Deprecated
    public GameWorldRepository() {
        super("world");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    " (worldId INTEGER auto_increment," +
                    "timezone VARCHAR(40)," +
                    "name VARCHAR(40) NOT NULL," +
                    "defaultPort INTEGER NOT NULL," +
                    "ip_address VARCHAR(50) NOT NULL," +
                    "enabled BOOLEAN NOT NULL," +
                    "CONSTRAINT pk_world PRIMARY KEY(worldId)," +
                    "CONSTRAINT uk_ip_port UNIQUE(defaultPort,ip_address)," +
                    "CONSTRAINT uk_name UNIQUE(name))");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createWorld(String worldname, String ip_address, int port, boolean enabled) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName
                    + "(name,defaultPort,ip_address,enabled) VALUES (?,?,?,?)");
            ps.setString(1, worldname);
            ps.setInt(2, port);
            ps.setString(3, ip_address);
            ps.setBoolean(4, enabled);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public GameWorld getWorld(String worldname) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName
                    + " WHERE name = ?");
            ps.setString(1, worldname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getGameWorldByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public GameWorld getWorld(int worldId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName
                    + " WHERE worldId = ?");
            ps.setInt(1, worldId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getGameWorldByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<GameWorld> getAllWorlds() {
        List<GameWorld> allWorlds = new ArrayList<>();
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allWorlds.add(getGameWorldByResultSetEntry(rs));
            }
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return allWorlds;
    }
    public List<GameWorld> getAllEnabledWorlds() {
        List<GameWorld> enabledWorlds = new ArrayList<>();
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName
                    + " WHERE enabled = ?");
            ps.setBoolean(1, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                enabledWorlds.add(getGameWorldByResultSetEntry(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enabledWorlds;
    }
    public List<GameWorld> getAllWorldsByAccount(int accountId) {
        List<GameWorld> worlds = new ArrayList<>();
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName
                    + " WHERE worldId IN (SELECT worldId FROM " + AccountPlayerRepository.getInstance().tableName + " WHERE accountId = ?)");
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                worlds.add(getGameWorldByResultSetEntry(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return worlds;
    }

    private GameWorld getGameWorldByResultSetEntry(ResultSet rs) throws SQLException {
        int worldId = rs.getInt("worldId");
        String worldname = rs.getString("name");
        String ip= rs.getString("ip_address");
        int defaultPort = rs.getInt("defaultPort");
        return new GameWorld(worldId,worldname,defaultPort,ip);
    }

}
