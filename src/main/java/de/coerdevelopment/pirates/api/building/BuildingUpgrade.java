package de.coerdevelopment.pirates.api.building;

import de.coerdevelopment.pirates.api.building.instances.Farm;
import de.coerdevelopment.pirates.api.building.instances.Lumberjack;
import de.coerdevelopment.pirates.api.building.instances.Mine;
import de.coerdevelopment.pirates.api.building.instances.Storage;

import java.util.ArrayList;
import java.util.List;

public class BuildingUpgrade {

    /**
     * Singleton in this case because initializing all buildings for every player will
     * cause much performance. Unless this object will not change over time, we can just
     * create the instance the first time we call it.
     */
    private static BuildingUpgrade instance;

    public static BuildingUpgrade getInstance() {
        if (instance == null) {
            instance = new BuildingUpgrade();
        }
        return instance;
    }

    public List<Lumberjack> lumberjackInfo = new ArrayList<>();
    public List<Mine> mineInfo = new ArrayList<>();
    public List<Farm> farmInfo = new ArrayList<>();
    public List<Storage> Storage = new ArrayList<>();

    private BuildingUpgrade() {
        for (int level = 1; level <= BuildingType.LUMBERJACK.getMaxLevel(); level++) {
            Lumberjack temp = new Lumberjack(level);
            temp.upgradeFinished = temp.getUpgradeTime(level);
            temp.maxResourceStorage = temp.getMaxResourceStorage(level);
            temp.productionRate = temp.getProductionRate(level);
        }
        for (int level = 1; level <= BuildingType.MINE.getMaxLevel(); level++) {
            Mine temp = new Mine(level);
            temp.upgradeFinished = temp.getUpgradeTime(level);
            temp.maxResourceStorage = temp.getMaxResourceStorage(level);
            temp.productionRate = temp.getProductionRate(level);
            temp.setUpgradeCosts(level);
        }
        for (int level = 1; level <= BuildingType.FARM.getMaxLevel(); level++) {
            Farm temp = new Farm(level);
            temp.upgradeFinished = temp.getUpgradeTime(level);
            temp.maxResourceStorage = temp.getMaxResourceStorage(level);
            temp.productionRate = temp.getProductionRate(level);
            temp.setUpgradeCosts(level);
        }
        for (int level = 1; level <= BuildingType.STORAGE.getMaxLevel(); level++) {
            Storage temp = new Storage(level);
            temp.upgradeFinished = temp.getUpgradeTime(level);
            temp.setUpgradeCosts(level);
        }
    }

}
