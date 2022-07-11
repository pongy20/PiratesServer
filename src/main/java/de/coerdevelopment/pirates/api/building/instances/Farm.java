package de.coerdevelopment.pirates.api.building.instances;

import de.coerdevelopment.pirates.api.ResourceType;
import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.pirates.api.building.ProductionBuilding;
import de.coerdevelopment.pirates.api.building.ResourceCost;

public class Farm extends ProductionBuilding {

    public Farm(int level) {
        super(BuildingType.FARM, level, ResourceType.FABRIC);
    }

    @Override
    public ResourceCost getUpgradeCosts(int level) {
        ResourceCost costs;
        switch (level) {
            case 1 -> costs = new ResourceCost(0,0,0,0);
            case 2 -> costs = new ResourceCost(50,50,10,0);
            case 3 -> costs = new ResourceCost(180,180,40,0);
            case 4 -> costs = new ResourceCost(500,500,100,0);
            case 5 -> costs = new ResourceCost(1000,1000,400,0);
            case 6 -> costs = new ResourceCost(2000,2000,800,0);
            case 7 -> costs = new ResourceCost(5000,5000,1200,0);
            case 8 -> costs = new ResourceCost(9000,9000,2000,0);
            case 9 -> costs = new ResourceCost(15000,15000,3000,0);
            case 10 -> costs = new ResourceCost(25000,25000,6000,0);
            case 11 -> costs = new ResourceCost(39000,39000,10000,0);
            case 12 -> costs = new ResourceCost(49000,49000,15000,0);
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

    @Override
    public int getMaxResourceStorage(int level) {
        int rate;
        switch (level) {
            case 1 -> rate = 100;
            case 2 -> rate = 150;
            case 3 -> rate = 200;
            case 4 -> rate = 300;
            case 5 -> rate = 400;
            case 6 -> rate = 500;
            case 7 -> rate = 750;
            case 8 -> rate = 1000;
            case 9 -> rate = 1250;
            case 10 -> rate = 1500;
            case 11 -> rate = 2000;
            case 12 -> rate = 2500;
            default -> throw new IllegalStateException("Unexpected value: " + level);
        }
        return rate;
    }

}
