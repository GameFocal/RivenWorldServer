package com.gamefocal.rivenworld.entites.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public class GsonUtil {

    public static Object getFromPath(JsonElement json, String path) {

        Map config = new Gson().fromJson(json, HashMap.class);

        Object pointer = config;

        for (String p : path.split("\\.")) {
            if (config.containsKey(p)) {
                if (pointer instanceof Map) {
                    pointer = ((Map) pointer).get(p);
                }
            } else {
                return null;
            }
        }

        return config;
    }

    public static JsonElement setFromPath(JsonElement json, String path, Object value) {

        Map config = new Gson().fromJson(json, HashMap.class);

        Object pointer = config;

        for (String p : path.split("\\.")) {
            if (config.containsKey(p)) {
                if (pointer instanceof Map) {
                    pointer = ((Map) pointer).get(p);
                }
            } else {
                return null;
            }
        }

        pointer = value;

        return new Gson().toJsonTree(pointer, HashMap.class);
    }

    public static JsonElement merge(JsonElement one, JsonElement two) {
        Map a = new Gson().fromJson(one, HashMap.class);
        Map b = new Gson().fromJson(two, HashMap.class);

        a.putAll(b);

        return new Gson().toJsonTree(a, HashMap.class);
    }

}
