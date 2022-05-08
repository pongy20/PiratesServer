package de.coerdevelopment.pirates.api.building.instances;

import de.coerdevelopment.pirates.api.building.Building;
import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.pirates.api.building.ResourceCost;

public class Storage extends Building {

    public Storage(int level) {
        super(BuildingType.STORAGE, level);
    }

    public ResourceCost getStorage(int level) {
        return new ResourceCost(100,100,100,0);
    }

    @Override
    public ResourceCost getUpgradeCosts(int level) {
        return new ResourceCost(100,100,100,0);
    }
}
