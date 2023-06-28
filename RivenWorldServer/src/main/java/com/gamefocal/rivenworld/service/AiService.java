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

        // Monistary
        this.blockCellsInArea("111874.73,70977.805,13798.965,0.0,0.0,44.311882", "115679.96,73680.18,13846.187,0.0,0.0,-120.18956");

        // Town Church
        this.blockCellsInArea("151558.1,91070.9,3584.176,0.0,0.0,-11.227478", "153977.73,88574.78,3397.317,0.0,0.0,155.58607");

        // Town Windmill
        this.blockCellsInArea("151055.73,82963.836,4276.4224,0.0,0.0,34.194824", "151878.92,83775.98,4276.4224,0.0,0.0,-142.51082");

        // Town Blacksmith
        this.blockCellsInArea("151574.95,82473.234,4276.283,0.0,0.0,-126.11322", "150768.05,80662.72,4276.283,0.0,0.0,41.510834");

        // General Store
        this.blockCellsInArea("152056.53,80381.33,4276.283,0.0,0.0,-38.91916", "153574.28,79164.53,4276.283,0.0,0.0,133.85257");

        // Misc
        this.blockCellsInArea("153855.27,80757.07,4276.283,0.0,0.0,7.490543", "155575.67,81066.81,4273.0923,0.0,0.0,-179.28065");
        this.blockCellsInArea("154459.33,83372.016,4276.0884,0.0,0.0,-65.75372", "155767.4,82058.945,4276.2627,0.0,0.0,133.53633");
        this.blockCellsInArea("156479.02,89154.52,3598.2668,0.0,0.0,123.54739", "155266.77,91279.15,3572.4314,0.0,0.0,-66.333374");
        this.blockCellsInArea("32674.74,134470.1,12006.78,0.0,0.0,-104.68651", "33472.402,132855.44,12006.308,0.0,0.0,99.98795");
        this.blockCellsInArea("35467.902,133482.56,12134.79,0.0,0.0,-31.855042", "36776.406,132164.47,12134.79,0.0,0.0,154.60771");
        this.blockCellsInArea("37272.25,134859.78,12153.051,0.0,0.0,31.237928", "38779.223,136082.23,12162.215,0.0,0.0,-152.73193");
        this.blockCellsInArea("35969.426,138184.23,12162.3,0.0,0.0,-95.41312", "35920.355,138252.12,12160.856,0.0,0.0,-91.36182");

        // Throne
        this.blockCellsInArea("72473.03,114062.555,25269.783,0.0,0.0,83.28983", "72464.84,116475.766,25269.783,0.0,0.0,-86.797485");
    }

    private void blockCellsInArea(String loc1, String loc2) {
        WorldCell a = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromString(loc1));
        WorldCell b = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromString(loc2));

        ArrayList<WorldCell> cells = DedicatedServer.instance.getWorld().getGrid().getCellsInRectangle(a, b);

        for (WorldCell cell : cells) {
            cell.setForceBlocked(true);
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
