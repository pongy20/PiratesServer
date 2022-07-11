package de.coerdevelopment.pirates.api.service.gameserver;

import de.coerdevelopment.pirates.api.Island;
import de.coerdevelopment.pirates.api.building.Building;
import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.pirates.api.building.ProductionBuilding;
import de.coerdevelopment.pirates.api.repository.gameserver.BuildingRepository;
import de.coerdevelopment.pirates.api.repository.gameserver.IslandRepository;

import java.util.List;

public class IslandService {
    
    private static IslandService instance;
    
    public static IslandService getInstance() {
        if (instance == null) {
            instance = new IslandService();
        }
        return instance;
    }

    private IslandRepository islandRepository;
    private BuildingRepository buildingRepository;

    public List<Island> islands;

    private IslandService() {

    }

    public int collectResources(Island island, BuildingType type) {
        ProductionBuilding building = (ProductionBuilding) island.getBuildingByType(type);
        building.setCurrentResourceStorage();
        island.addCurrentlyStoredResourceByType(building.resourceType, building.currentResourcesStored);
        int toReturn = building.currentResourcesStored;
        building.currentResourcesStored = 0;
        return toReturn;
    }

    public synchronized Island getIslandById(int islandId) {
        for (Island island : islands) {
            if (island.islandId == islandId) {
                return island;
            }
        }
        Island island = islandRepository.getIslandById(islandId);
        islands.add(island);
        return island;
    }

}
