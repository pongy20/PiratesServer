package de.coerdevelopment.pirates.api.repository.gameserver;

import de.coerdevelopment.pirates.api.Island;
import de.coerdevelopment.pirates.api.Player;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Table syntax:
 *
 * islandId Integer PK
 * owner Integer NN --> FK (playerId)
 * name VARCHAR(32) UQ NN
 * x Integer NN
 * y Integer NN
 * ducat Integer nullable
 */
public class IslandRepository extends Repository {

    private static IslandRepository instance;
    
    public static IslandRepository getInstance() {
        if (instance == null) {
            instance = new IslandRepository();
        }
        return instance;
    }
    
    private IslandRepository() {
        super("island");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    " (islandId INTEGER AUTO_INCREMENT," +
                    "owner INTEGER NOT NULL," +
                    "name VARCHAR(32) NOT NULL," +
                    "x INTEGER NOT NULL," +
                    "y INTEGER NOT NULL," +
                    "ducat INTEGER," +
                    "CONSTRAINT pk_island PRIMARY KEY(islandId)," +
                    "CONSTRAINT fk_owner FOREIGN KEY(owner) REFERENCES " + PlayerRepository.getInstance().tableName + "(playerId)," +
                    "CONSTRAINT uq_name UNIQUE(name))");
            ps.execute();
            PreparedStatement ps2 = sql.getConnection().prepareStatement("ALTER TABLE " + tableName + " AUTO_INCREMENT = 3000000");
            ps2.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Island createIsland(int playerId, String islandName) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName +
                    " (owner,name,x,y,ducat) VALUES (?,?,?,?,?)");
            ps.setInt(1, playerId);
            ps.setString(2, islandName);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getIslandByName(islandName);
    }

    public void updateOwner(int islandId, int newOwner) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName + " SET owner = ? WHERE islandId = ?");
            ps.setInt(1, newOwner);
            ps.setInt(2, islandId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateName(int islandId, String newName) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName + " SET name = ? WHERE islandId = ?");
            ps.setString(1, newName);
            ps.setInt(2, islandId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDucat(int islandId, int changeDucatValue) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName + " SET ducat = ducat + ? WHERE islandId = ?");
            ps.setInt(1, changeDucatValue);
            ps.setInt(2, islandId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesNameExists(String name) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT name FROM " + tableName + " WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Island getIslandByName(String islandName) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName + " WHERE islandName = ?");
            ps.setString(1, islandName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getIslandByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Island getIslandById(int islandId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName + " WHERE islandId = ?");
            ps.setInt(1, islandId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return getIslandByResultSetEntry(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Island> getIslandsByPlayer(int playerId) {
        List<Island> islands = new ArrayList<>();
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName + " WHERE owner = ?");
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                islands.add(getIslandByResultSetEntry(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return islands;
    }

    private Island getIslandByResultSetEntry(ResultSet rs) throws SQLException {
        int islandId = rs.getInt("islandId");
        int playerId = rs.getInt("owner");
        String islandName = rs.getString("islandName");
        int x = rs.getInt("x");
        int y = rs.getInt("y");
        int ducat = rs.getInt("ducat");
        //TODO: load buildings
        Player player = PlayerRepository.getInstance().getPlayerByPlayerId(playerId);
        Island island = new Island(islandId, player, islandName, ducat, null, null, null, null);
        return island;
    }

}
