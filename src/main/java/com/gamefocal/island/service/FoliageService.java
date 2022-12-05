package com.gamefocal.island.service;

import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.entites.trees.TreeLarge;
import com.gamefocal.island.game.entites.trees.TreeMedium;
import com.gamefocal.island.game.entites.trees.TreeSapling;
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
import java.util.Hashtable;
import java.util.UUID;

@Singleton
@AutoService(HiveService.class)
public class FoliageService implements HiveService<FoliageService> {

    private JsonArray foliageCache = new JsonArray();

    public static String getHash(String name, Location location) {
        return DigestUtils.md5Hex(name + "" + Math.round(location.getX()) + "" + Math.round(location.getY()) + "" + Math.round(location.getZ()));
    }

    @Override
    public void init() {
        try {
            if (DataService.gameFoliage.countOf() == 0) {
                // No foliage we need to load it now.

                System.out.println("Loading Foliage...");

                Gson g = new Gson();

                ClassLoader classloader = Thread.currentThread().getContextClassLoader();

                InputStream in = classloader.getResourceAsStream("foliage.json");
                if (in != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                        // Default
                        String defaultFoliage = IOUtils.toString(reader);

                        JsonArray a = JsonParser.parseString(defaultFoliage).getAsJsonArray();

                        float total = a.size();

                        System.out.println("Loading " + total + " Foliage Items...");

                        for (JsonElement o1 : a) {
                            JsonObject o = o1.getAsJsonObject();

                            GameFoliageModel m = new GameFoliageModel();
                            m.hash = o.get("hash").getAsString();
                            m.modelName = o.get("obj").getAsString();
                            m.growth = 100.00f;
                            m.health = 100.00f;
                            m.foliageState = FoliageState.GROWN;
                            m.location = Location.fromString(o.get("loc").getAsString());

                            DataService.gameFoliage.createIfNotExists(m);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void register(String name, Location location) {
        String hash = getHash(name, location);

        JsonObject f = new JsonObject();
        f.addProperty("hash", hash);
        f.addProperty("obj", name);
        f.addProperty("loc", location.toString());

        this.foliageCache.add(f);
    }

    public JsonArray getFoliageCache() {
        return foliageCache;
    }
}
