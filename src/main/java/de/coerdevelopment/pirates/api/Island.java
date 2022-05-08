package de.coerdevelopment.pirates.api;

import de.coerdevelopment.pirates.api.building.Building;
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
    public List<Building> buildings;

    public Island(int islandId, Player owner, String islandName, List<Building> buildings) {
        this.islandId = islandId;
        this.owner = owner;
        this.islandName = islandName;
        this.buildings = buildings;
        this.coordinates = new Coordinates(0,0);
    }

    public static List<Building> getBasicBuildings() {
        return Arrays.asList(new Lumberjack(1), new Mine(1), new Farm(1), new Storage(1));
    }
}
