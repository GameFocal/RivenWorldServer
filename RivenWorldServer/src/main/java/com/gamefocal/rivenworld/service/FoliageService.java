package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.service.HiveService;
import com.google.auto.service.AutoService;
import com.google.gson.*;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Singleton;

@Singleton
@AutoService(HiveService.class)
public class FoliageService implements HiveService<FoliageService> {

    private JsonArray foliageCache = new JsonArray();

    public static String getHash(String name, String locStr) {
        return DigestUtils.md5Hex(name + ":" + locStr);
    }

    @Override
    public void init() {
    }

    public float getStartingHealth(String name) {
        if (name.contains("Medium")) {
            // Media Tree
            return 100;
        } else if (name.contains("Large")) {
            // Large Tree
            return 250;
        } else if (name.contains("Saplings")) {
            // Small Tree
            return 15;
        }

        return 25;
    }

    public JsonArray getFoliageCache() {
        return foliageCache;
    }
}
