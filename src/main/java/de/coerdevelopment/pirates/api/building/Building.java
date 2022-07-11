package de.coerdevelopment.pirates.api.building;

public abstract class Building {

    public BuildingType type;
    public int level;
    public long upgradeFinished;
    public boolean isUpgrading;
    public int woodCosts;
    public int oreCosts;
    public int fabricCosts;

    public Building(BuildingType type, int level) {
        this.type = type;
        this.level = level;
        setUpgradeCosts(level);
    }

    public abstract ResourceCost getUpgradeCosts(int level);

    public boolean isMaxed() {
        return level >= type.getMaxLevel();
    }

    public void setUpgradeCosts(int level) {
        ResourceCost costs = getUpgradeCosts(level);
        this.woodCosts = costs.wood.amount;
        this.oreCosts = costs.ore.amount;
        this.fabricCosts = costs.fabric.amount;
    }

    public long getUpgradeTime(int level) {
        //TODO: Have to be replaced in every building instance, just for test reason
        return switch (level) {
          case 2 -> 1000*60;
          case 3 -> 1000*60*5;
          case 4 -> 1000*60*15;
          case 5 -> 1000*60*60;
          case 6 -> 1000*60*60*2;
          case 7 -> 1000*60*60*4;
          case 8 -> 1000*60*60*8;
          case 9 -> 1000*60*60*12;
          case 10 -> 1000*60*60*24;
          case 11 -> 1000*60*60*24*2;
          case 12 -> 1000*60*60*24*4;
          default -> 0;
        };
    }

}
