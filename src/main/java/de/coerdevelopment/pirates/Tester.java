package de.coerdevelopment.pirates;

import com.google.gson.Gson;
import de.coerdevelopment.pirates.api.GameWorld;

import java.util.HashMap;
import java.util.Map;

public class Tester {

    public static void main(String[] args) {
        Gson gson = new Gson();
        Map<GameWorld, String> worlds = new HashMap<>();
        GameWorld world1 = new GameWorld("test", 10, "ip");
        worlds.put(world1, "Test123");
        String json = gson.toJson(worlds);
        System.out.println(json);
    }

}
