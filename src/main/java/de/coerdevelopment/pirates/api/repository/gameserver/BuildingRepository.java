package de.coerdevelopment.pirates.api.repository.gameserver;

import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Table syntax:
 *
 * islandId Integer PK --> FK
 * buildingType VARCHAR(32) PK --> FK
 * level Integer NN
 */
public class BuildingRepository extends Repository {

    private static BuildingRepository instance;

    public static BuildingRepository getInstance() {
        if (instance == null) {
            instance = new BuildingRepository();
        }
        return instance;
    }

    private BuildingRepository() {
        super("building");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    " (islandId INTEGER," +
                    "buildingType VARCHAR(32)," +
                    "level INTEGER NOT NULL," +
                    "CONSTRAINT pk_bd PRIMARY KEY(islandId,buildingType)," +
                    "CONSTRAINT fk_islandId FOREIGN KEY(islandId) REFERENCES " + IslandRepository.getInstance().tableName + "(islandId) ON DELETE CASCADE," +
                    "CONSTRAINT fk_buildingType FOREIGN KEY(buildingType) REFERENCES " + BuildingTypeRepository.getInstance().tableName + "(buildingType) ON DELETE CASCADE)");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBuilding(int islandId, BuildingType type) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName +
                    " (islandId,buildingType,level) VALUES (?,?,?)");
            ps.setInt(1, islandId);
            ps.setString(2, type.name());
            ps.setInt(3, 1);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upgradeBuildingLevel(int islandId, BuildingType type) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName + " SET level = level + 1 " +
                    "WHERE islandId = ? AND type = ?");
            ps.setInt(1, islandId);
            ps.setString(2, type.name());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLevel(int islandId, BuildingType type) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("SELECT level FROM " + tableName + " WHERE islandId = ? AND buildingType = ?");
            ps.setInt(1, islandId);
            ps.setString(2, type.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("level");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
