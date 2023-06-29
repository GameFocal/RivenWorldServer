package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiSpawn;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.living.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.google.auto.service.AutoService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
@AutoService(HiveService.class)
public class AiService implements HiveService<AiService> {

    public static HashMap<String, LivingEntity> types = new HashMap<>();
    public static Long lastAiSpawnCheck = 0L;
    public ConcurrentLinkedQueue<UUID> trackedEntites = new ConcurrentLinkedQueue<>();
    public ConcurrentHashMap<String, AiSpawn> spawners = new ConcurrentHashMap<>();
    public ConcurrentHashMap<UUID, GameEntity> lightSources = new ConcurrentHashMap<>();
    private ExecutorService executor;

    public static void exec(Runnable task) {
        DedicatedServer.get(AiService.class).executor.submit(task);
    }

    public void exportToFile() {
        JsonObject object = DedicatedServer.gson.toJsonTree(this.spawners, ConcurrentHashMap.class).getAsJsonObject();
        try {
            Files.writeString(Path.of("spawners.json"), object.toString(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromJar() {

        System.out.println("Loading AI Spawners...");

        try {
            InputStream s = getClass().getClassLoader().getResourceAsStream("spawners.json");
            try {
                JsonObject object = JsonParser.parseString(new String(s.readAllBytes())).getAsJsonObject();

                for (Map.Entry<String, JsonElement> e : object.entrySet()) {
                    AiSpawn spawn = DedicatedServer.gson.fromJson(e.getValue(), AiSpawn.class);
                    if (spawn != null) {
                        this.spawners.put(e.getKey(), spawn);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("No Spawners File Found");
        }
    }

    @Override
    public void init() {
        this.executor = Executors.newFixedThreadPool(4);

        types.put("deer", new Deer());
        types.put("doe", new Doe());
        types.put("rabbit", new Rabbit());
        types.put("bear", new Bear());
        types.put("boar", new Boar());
        types.put("undead", new Undead());

        this.loadFromJar();
    }

    public void blockCellsInArea(String loc1, String loc2) {
        WorldCell a = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromString(loc1));
        WorldCell b = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromString(loc2));

        ArrayList<WorldCell> cells = DedicatedServer.instance.getWorld().getGrid().getCellsInRectangle(a, b);

        for (WorldCell cell : cells) {
            cell.setForceBlocked(true);
            cell.refresh();
        }
    }

    public void processAiTick() {
        for (UUID uuid : this.trackedEntites) {
            GameEntityModel gameEntity = DedicatedServer.instance.getWorld().getEntityFromId(uuid);
            if (gameEntity != null && gameEntity.entityData != null) {
                gameEntity.entityData.onTick();

                WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(gameEntity.entityData.location);

                /*
                 * Process update event
                 * */
//                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                    if (connection.isLoaded()) {
//                        // All players
//                        if (connection.getLoadedChunks().containsKey(chunk.getChunkCords().toString())) {
//                            float lod = connection.getLOD(gameEntity.entityData.location);
//                            if (gameEntity.entityData.spacialLOD >= lod) {
//                                connection.syncEntity(gameEntity, DedicatedServer.instance.getWorld().getChunk(gameEntity.entityData.location), true, true);
//                            } else {
//                                connection.despawnEntity(gameEntity, DedicatedServer.instance.getWorld().getChunk(gameEntity.entityData.location), true);
//                            }
//                        }
//                    }
//                }

            }
        }
    }
}
