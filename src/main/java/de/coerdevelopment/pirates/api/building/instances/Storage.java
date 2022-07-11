package de.coerdevelopment.pirates.api.building.instances;

import de.coerdevelopment.pirates.api.ResourceType;
import de.coerdevelopment.pirates.api.building.Building;
import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.pirates.api.building.ResourceCost;

public class Storage extends Building {

    public int woodStorage;
    public int oreStorage;
    public int fabricStorage;

    public Storage(int level) {
        super(BuildingType.STORAGE, level);
        int storage = getStorageByLevel(level);
        woodStorage = storage;
        oreStorage = storage;
        fabricStorage = storage;
    }

    public int getStorage(ResourceType type) {
        return switch (type) {
            case WOOD -> woodStorage;
            case ORE -> oreStorage;
            case FABRIC -> fabricStorage;
            default -> 0;
        };
    }

    public int getStorageByLevel(int level) {
        return switch (level) {
            case 1 -> 1000;
            case 2 -> 1500;
            case 3 -> 2500;
            case 4 -> 5000;
            case 5 -> 10000;
            case 6 -> 15000;
            case 7 -> 20000;
            case 8 -> 30000;
            case 9 -> 45000;
            case 10 -> 60000;
            case 11 -> 75000;
            case 12 -> 100000;
            default -> 0;
        };
    }

    @Override
    public ResourceCost getUpgradeCosts(int level) {
        return new ResourceCost(100,100,100,0);
    }
}
