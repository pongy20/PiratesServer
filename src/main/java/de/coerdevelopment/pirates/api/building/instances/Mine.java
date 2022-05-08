package de.coerdevelopment.pirates.api.building.instances;

import de.coerdevelopment.pirates.api.ResourceType;
import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.pirates.api.building.ProductionBuilding;
import de.coerdevelopment.pirates.api.building.ResourceCost;

public class Mine extends ProductionBuilding {

    public Mine(int level) {
        super(BuildingType.MINE, level, ResourceType.ORE);
    }

    @Override
    public ResourceCost getUpgradeCosts(int level) {
        ResourceCost costs;
        switch (level) {
            case 1 -> costs = new ResourceCost(0,0,0,0);
            case 2 -> costs = new ResourceCost(20,50,0,0);
            case 3 -> costs = new ResourceCost(100,200,0,0);
            case 4 -> costs = new ResourceCost(210,550,40,0);
            case 5 -> costs = new ResourceCost(500,1000,100,0);
            case 6 -> costs = new ResourceCost(1200,2500,200,0);
            case 7 -> costs = new ResourceCost(2300,5100,400,0);
            case 8 -> costs = new ResourceCost(5600,11000,900,0);
            case 9 -> costs = new ResourceCost(8700,19000,1900,0);
            case 10 -> costs = new ResourceCost(12000,32000,3400,0);
            case 11 -> costs = new ResourceCost(21000,43000,6000,0);
            case 12 -> costs = new ResourceCost(30000,50000,10000,0);
            default -> throw new IllegalStateException("Unexpected value: " + level);
        }
        return costs;
    }

    @Override
    public int getProductionRate(int level) {
        int rate;
        switch (level) {
            case 1 -> rate = 30;
            case 2 -> rate = 50;
            case 3 -> rate = 75;
            case 4 -> rate = 110;
            case 5 -> rate = 180;
            case 6 -> rate = 250;
            case 7 -> rate = 350;
            case 8 -> rate = 450;
            case 9 -> rate = 600;
            case 10 -> rate = 750;
            case 11 -> rate = 950;
            case 12 -> rate = 1200;
            default -> throw new IllegalStateException("Unexpected value: " + level);
        }
        return rate;
    }

}
