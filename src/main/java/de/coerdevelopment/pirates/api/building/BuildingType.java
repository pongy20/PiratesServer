package de.coerdevelopment.pirates.api.building;

public enum BuildingType {

    LUMBERJACK(12),
    MINE(12),
    FARM(12),
    FISHING_HUT(10),
    KONTOR(5),
    TAVERN(8),
    STORAGE(12),
    HARBOR(5),
    SHIPPING_COMPANY(8),
    SHIPYARD(5),
    DEFENSE_TOWER(12);

    private int maxLevel;

    private BuildingType(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
