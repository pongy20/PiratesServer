package de.coerdevelopment.pirates.api.building;

import de.coerdevelopment.pirates.api.ResourceType;

public abstract class ProductionBuilding extends Building {

    public ResourceType resourceType;
    public int productionRate;

    public ProductionBuilding(BuildingType type, int level, ResourceType resourceType) {
        super(type, level);
        this.resourceType = resourceType;
        this.productionRate = getProductionRate(level);
    }

    public abstract int getProductionRate(int level);

}
