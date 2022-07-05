package de.coerdevelopment.pirates.api.repository.gameserver;

import de.coerdevelopment.pirates.api.Player;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Table syntax:
 *
 * playerId Integer PK --> FK
 * score Integer NN
 */
public class PlayerRankRepository extends Repository {
    
    private static PlayerRankRepository instance;
    
    public static PlayerRankRepository getInstance() {
        if (instance == null) {
            instance = new PlayerRankRepository();
        }
        return instance;
    }
    
    private PlayerRankRepository() {
        super("player_rank");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    " (playerId Integer," +
                    "score Integer NOT NULL," +
                    "CONSTRAINT pk_rank PRIMARY KEY(playerId)," +
                    "CONSTRAINT fk_playerId FOREIGN KEY(playerId) REFERENCES " + PlayerRepository.getInstance().tableName + "(playerId) ON DELETE CASCADE)");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerListed(int playerId) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT playerId FROM " + tableName + " WHERE playerId = ?");
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void insertScore(int playerId, int score) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName + " (playerId,score) VALUES (?,?)");
            ps.setInt(1, playerId);
            ps.setInt(2, score);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateValue(int playerId, int score) {
        if (!isPlayerListed(playerId)) {
            insertScore(playerId, score);
            return;
        }
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName + " SET score = ? WHERE playerId = ?");
            ps.setInt(1, score);
            ps.setInt(2, playerId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getRank(int playerId) {
       try {
           PreparedStatement ps = sql.getConnection().prepareStatement("SELECT score FROM " + tableName + " WHERE playerId = ?");
           ps.setInt(1, playerId);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getInt("score");
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return 0;
    }

}
