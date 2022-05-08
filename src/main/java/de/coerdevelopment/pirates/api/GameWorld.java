package de.coerdevelopment.pirates.api;

import java.io.Serializable;
import java.util.TimeZone;

public class GameWorld implements Serializable {

    public int worldId;
    public String worldname;
    public int port;
    public String networkAddress;

    public GameWorld(String worldname, int port, String networkAddress) {
        this(-1, worldname, port, networkAddress);
    }
    public GameWorld(int worldId, String worldname, int port, String networkAddress) {
        this.worldId = worldId;
        this.worldname = worldname;
        this.port = port;
        this.networkAddress = networkAddress;
    }
}
