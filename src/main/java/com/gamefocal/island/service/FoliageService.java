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

    private Hashtable<String, GameEntity> foliageEntites = new Hashtable<>();

    public static String getHash(String name, Location location) {
        return DigestUtils.md5Hex(name + location);
    }

    @Override
    public void init() {
        try {
            if (DataService.gameFoliage.countOf() == 0) {
                // No foliage we need to load it now.

                System.out.println("Loading Foliage...");

                Gson g = new Gson();

                ClassLoader classloader = Thread.currentThread().getContextClassLoader();

                try (InputStream in = classloader.getResourceAsStream("foliage.json");
                     BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

                    // Default
                    String defaultFoliage = IOUtils.toString(reader);

                    JsonArray a = JsonParser.parseString(defaultFoliage).getAsJsonArray();

                    int totalItemsToLoad = a.size();
                    int i = 0;

                    for (JsonElement o1 : a) {
                        JsonObject o = o1.getAsJsonObject();

                        Class<?> c = Class.forName(o.get("foliageType").getAsString());
                        GameEntity<?> e = (GameEntity<?>) c.newInstance();

                        GameFoliageModel f = new GameFoliageModel();
                        f.hash = o.get("hash").getAsString();
                        f.foliageType = o.get("foliageType").getAsString();
                        f.foliageState = FoliageState.valueOf(o.get("foliageState").getAsString());
                        f.health = o.get("health").getAsFloat();
                        f.location = g.fromJson(o.get("location").getAsJsonObject(), Location.class);
                        f.attachedEntity = e;

                        DataService.gameFoliage.createIfNotExists(f);

                        System.out.println("[Foliage]: " + ++i + "/" + totalItemsToLoad);
                    }

                } catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void register(String name, Location location) {
        String hash = getHash(name, location);

        GameEntity e = null;

        if (!foliageEntites.containsKey(hash)) {
            if (name.contains("TreeFirLarge")) {
                e = new TreeLarge();
            } else if (name.contains("TreeFirSaplings")) {
                e = new TreeSapling();
            } else if (name.contains("TreeFirMedium")) {
                e = new TreeMedium();
            } else {
                System.out.println("Unknown Tree: " + name);
            }


            if (e != null) {
                // Is a valid entity
                e.uuid = UUID.randomUUID();
                e.location = location;

                this.foliageEntites.put(hash, e);
            }
        }
    }

    public Hashtable<String, GameEntity> getFoliageEntites() {
        return foliageEntites;
    }
}
