package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameFoliageModel;
import com.google.auto.service.AutoService;
import com.google.gson.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

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
