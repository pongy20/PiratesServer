package de.coerdevelopment.pirates.api.building;

public abstract class Building {

    public BuildingType type;
    public int level;

    public Building(BuildingType type, int level) {
        this.type = type;
        this.level = level;
    }

    public abstract ResourceCost getUpgradeCosts(int level);

    public boolean isMaxed() {
        return level >= type.getMaxLevel();
    }

}
