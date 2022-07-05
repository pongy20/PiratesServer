package de.coerdevelopment.pirates.api.repository.gameserver;

import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.standalone.repository.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Table syntax:
 *
 * buildingType VARCHAR(32) PK
 */
public class BuildingTypeRepository extends Repository {

    private static BuildingTypeRepository instance;

    public static BuildingTypeRepository getInstance() {
        if (instance == null) {
            instance = new BuildingTypeRepository();
        }
        return instance;
    }

    private BuildingTypeRepository() {
        super("building_type");
    }

    public void createTable() {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    " (buildingType VARCHAR(32)," +
                    "CONSTRAINT pk_bdt PRIMARY KEY(buildingType))");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertDefaults() {
        for (BuildingType type : BuildingType.values()) {
            try {
                PreparedStatement ps = sql.getConnection().prepareStatement("INSERT INTO " + tableName +
                        " (buildingType) VALUES (?)");
                ps.setString(1, type.name());
            } catch (SQLException e) {
                continue;   // Entry already exists!
            }
        }
    }

}
