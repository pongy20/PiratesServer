package de.coerdevelopment.pirates.api.repository.gameserver;

import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Table syntax:
 *
 * playerId Integer PK --> FK
 * logintime BigInt PK
 */
public class PlayerLoginRepository extends Repository {

    private static PlayerLoginRepository instance;

    public static PlayerLoginRepository getInstance() {
        if (instance == null) {
            instance = new PlayerLoginRepository();
        }
        return instance;
    }

    private PlayerLoginRepository() {
        super("player_login");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    " (playerId INTEGER," +
                    "logintime BIGINT," +
                    "CONSTRAINT pk_lt PRIMARY KEY(playerId,logintime)," +
                    "CONSTRAINT fk_plid FOREIGN KEY (playerId) REFERENCES " + PlayerRepository.getInstance().tableName + "(playerId) ON DELETE CASCADE)");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertLogin(int playerId, long time) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName +
                    " (playerId,logintime) VALUES (?,?)");
            ps.setInt(1, playerId);
            ps.setLong(2, time);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Long> getRecentLogins(int playerId, int maxEntries) {
        List<Long> logins = new ArrayList<>();
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT logintime FROM " + tableName +
                    " WHERE playerId = ? " +
                    "ORDER BY logintime DESC LIMIT " + maxEntries);
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                logins.add(rs.getLong("logintime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logins;
    }
    public long getLastLogin(int playerId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT logintime FROM " + tableName +
                    " WHERE playerId = ? " +
                    "ORDER BY logintime DESC LIMIT 1");
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("logintime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

}
