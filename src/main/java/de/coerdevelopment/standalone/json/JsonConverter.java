package de.coerdevelopment.standalone.json;

import com.google.gson.Gson;
import de.coerdevelopment.standalone.net.Datapackage;

import java.util.Arrays;
import java.util.List;

public class JsonConverter {

    private static JsonConverter instance;

    public static JsonConverter getInstance() {
        if(instance == null) instance = new JsonConverter();
        return instance;
    }

    private JsonConverter() {}

    public synchronized String convertObjectToJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    public synchronized String convertDatapackageToJson(Datapackage datapackage) {
        Gson gson = new Gson();
        return gson.toJson(datapackage);
    }
    public synchronized Object convertJsonToObject(String jsonInput) {
        Gson gson = new Gson();
        return gson.fromJson(jsonInput, Datapackage.class);
    }
    public synchronized Object convertJsonToObject(String jsonInput, Class<?> cls) {
        Gson gson = new Gson();
        return gson.fromJson(jsonInput, cls);
    }
    public synchronized <T> List<T> convertJsonToList(String jsonInput, Class<T[]> cls) {
        Gson gson = new Gson();
        return Arrays.asList(gson.fromJson(jsonInput, cls));
    }

}
