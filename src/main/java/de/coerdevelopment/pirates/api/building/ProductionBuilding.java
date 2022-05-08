package de.coerdevelopment.pirates.api.building;

import de.coerdevelopment.pirates.api.ResourceType;

public abstract class ProductionBuilding extends Building {

    public ResourceType resourceType;

    public ProductionBuilding(BuildingType type, int level, ResourceType resourceType) {
        super(type, level);
        this.resourceType = resourceType;
    }

    public abstract int getProductionRate(int level);

}
