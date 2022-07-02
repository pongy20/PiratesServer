package de.coerdevelopment.pirates.api.building.instances;

import de.coerdevelopment.pirates.api.building.Building;
import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.pirates.api.building.ResourceCost;

public class Storage extends Building {

    public int woodStorage;
    public int oreStorage;
    public int fabricStorage;

    public Storage(int level) {
        super(BuildingType.STORAGE, level);
        woodStorage = 20000;
        oreStorage = 20000;
        fabricStorage = 20000;
    }

    public ResourceCost getStorage(int level) {
        return new ResourceCost(100,100,100,0);
    }

    @Override
    public ResourceCost getUpgradeCosts(int level) {
        return new ResourceCost(100,100,100,0);
    }
}
