package de.coerdevelopment.pirates.api.building;

import de.coerdevelopment.pirates.api.ResourceType;

public abstract class ProductionBuilding extends Building {

    public ResourceType resourceType;
    public int productionRate;
    public int currentResourcesStored;
    public int maxResourceStorage;

    private long lastTimeCollected; // time in milliseconds the resources have been collected
    private float excessResources;  // amount of resources which have been produced but are less than 1 --> will always be < 1

    public ProductionBuilding(BuildingType type, int level, ResourceType resourceType) {
        super(type, level);
        this.resourceType = resourceType;
        this.productionRate = getProductionRate(level);
        this.maxResourceStorage = getMaxResourceStorage(level);
        currentResourcesStored = 0;
        lastTimeCollected = System.currentTimeMillis();
    }

    public abstract int getProductionRate(int level);

    public abstract int getMaxResourceStorage(int level);

    public float getGeneratedResources() {
        float generatedResourcesPerSecond = ((float) productionRate) / 60 / 60;
        long secondsLast = (System.currentTimeMillis() / 1000) - (lastTimeCollected / 1000);
        return generatedResourcesPerSecond * secondsLast + excessResources;
    }

    public void setCurrentResourceStorage() {
        float generatedF = getGeneratedResources();
        lastTimeCollected = System.currentTimeMillis();
        int generated = (int) generatedF;
        excessResources = generatedF - ((float) generated);
        currentResourcesStored += generated;
        if (currentResourcesStored > maxResourceStorage) {
            currentResourcesStored = maxResourceStorage;
        }
    }

    public void setLastTimeCollected(long lastTimeCollected) {
        this.lastTimeCollected = lastTimeCollected;
    }

    public long getLastTimeCollected() {
        return lastTimeCollected;
    }

    public float getExcessResources() {
        return excessResources;
    }

    public void setExcessResources(float excessResources) {
        this.excessResources = excessResources;
    }
}
