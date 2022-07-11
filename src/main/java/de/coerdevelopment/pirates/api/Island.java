package de.coerdevelopment.pirates.api;

import de.coerdevelopment.pirates.api.building.Building;
import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.pirates.api.building.ProductionBuilding;
import de.coerdevelopment.pirates.api.building.instances.Farm;
import de.coerdevelopment.pirates.api.building.instances.Lumberjack;
import de.coerdevelopment.pirates.api.building.instances.Mine;
import de.coerdevelopment.pirates.api.building.instances.Storage;

import java.util.Arrays;
import java.util.List;

public class Island {

    public int islandId;
    public Player owner;
    public Coordinates coordinates;
    public String islandName;
    public int wood;
    public int ore;
    public int fabric;
    public int ducat;
    public List<Building> buildings;
    public Lumberjack lumberjack;
    public Mine mine;
    public Farm farm;
    public Storage storage;

    public Island(int islandId, Player owner, String islandName, int ducat, Lumberjack lumberjack, Mine mine, Farm farm, Storage storage) {
        this.islandId = islandId;
        this.owner = owner;
        this.islandName = islandName;
        this.ducat = ducat;
        this.lumberjack = lumberjack;
        this.mine = mine;
        this.farm = farm;
        this.storage = storage;
        this.coordinates = new Coordinates(0,0);
    }

    public static List<Building> getBasicBuildings() {
        return Arrays.asList(new Lumberjack(1), new Mine(1), new Farm(1), new Storage(1));
    }

    public int getCurrentlyStoredResourceByType(ResourceType type) {
        return switch (type) {
            case WOOD -> wood;
            case ORE -> ore;
            case FABRIC -> fabric;
            case DUCAT -> ducat;
            default -> 0;
        };
    }

    public void setCurrentlyStoredResourceByType(ResourceType type, int amount) {
        switch (type) {
            case WOOD ->  wood = amount;
            case ORE -> ore = amount;
            case FABRIC -> fabric = amount;
            case DUCAT -> ducat = amount;
        }
    }

    public void addCurrentlyStoredResourceByType(ResourceType type, int amount) {
        switch (type) {
            case WOOD ->  wood += amount;
            case ORE -> ore += amount;
            case FABRIC -> fabric += amount;
            case DUCAT -> ducat += amount;
        }
    }

    public Building getBuildingByType(BuildingType type) {
        return switch (type) {
            case LUMBERJACK -> lumberjack;
            case MINE -> mine;
            case FARM -> farm;
            case STORAGE -> storage;
            default -> null;
        };
    }

    public List<ProductionBuilding> getProductionBuildings() {
        return Arrays.asList(lumberjack, mine, farm);
    }

}
