package com.gamefocal.rivenworld.game.generator.basic;

import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.world.World;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.entites.resources.ground.*;
import com.gamefocal.rivenworld.game.entites.resources.nodes.*;
import com.gamefocal.rivenworld.game.generator.WorldLayerGenerator;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.models.GameResourceNode;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.ResourceService;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.generator.util.Generators;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class MineralLayer implements WorldLayerGenerator {
    @Override
    public void generateLayer(World world) {

        /*
         * Load the spawn locations from the .jar
         * */
        // Load in the items.json
        InputStream s = getClass().getClassLoader().getResourceAsStream("resource-nodes.json");
        try {
            JsonObject object = JsonParser.parseString(new String(s.readAllBytes())).getAsJsonObject();

            for (Map.Entry<String, JsonElement> m : object.entrySet()) {

                ResourceNodeEntity nodeEntity = null;
                GameEntity miniNode = null;
                int respawnTime = 5;
                if (m.getKey().equalsIgnoreCase("IronNode")) {
                    nodeEntity = new IronNode();
                    miniNode = new IronRockEntity();
                    respawnTime = 25;
                } else if (m.getKey().equalsIgnoreCase("OilNode")) {
                    nodeEntity = new OilNode();
//                    miniNode = new IronRockEntity();
                    respawnTime = 25;
                } else if (m.getKey().equalsIgnoreCase("CoalNode")) {
                    nodeEntity = new CoalNode();
                    miniNode = new CoalRockEntity();
                    respawnTime = 25;
                } else if (m.getKey().equalsIgnoreCase("DirtNode")) {
                    nodeEntity = new DirtNode();
//                    miniNode = new IronRockEntity();
                    respawnTime = 5;
                } else if (m.getKey().equalsIgnoreCase("GoldNode")) {
                    nodeEntity = new GoldNode();
                    miniNode = new GoldRockEntity();
                    respawnTime = 25;
                } else if (m.getKey().equalsIgnoreCase("RockNode")) {
                    nodeEntity = new RockNode();
                    miniNode = new SmallRockEntity();
                    respawnTime = 10;
                } else if (m.getKey().equalsIgnoreCase("CopperNode")) {
                    nodeEntity = new CopperNode();
                    miniNode = new CopperRockEntity();
                    respawnTime = 25;
                } else if (m.getKey().equalsIgnoreCase("SandNode")) {
                    nodeEntity = new SandNode();
                    respawnTime = 5;
                }

                if (nodeEntity != null) {
                    JsonArray locs = m.getValue().getAsJsonArray();
                    for (JsonElement e : locs) {
                        Location loc = Location.fromString(e.getAsString());

                        DedicatedServer.get(ResourceService.class).addNode(nodeEntity, loc, respawnTime);

                        System.out.println("SPAWN " + nodeEntity.getClass().getSimpleName() + " at " + loc);

//                        if (loc != null && miniNode != null) {
//                            // Spawn random rocks around this node
//                            this.generateEntity(world, loc, 300, .45f, miniNode, respawnTime, .45f, 9500, 25000);
//                        }

                    }
                } else {
                    System.err.println("Node NULL");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void generateEntity(World world, Location center, float radiusInUnits, float chance, GameEntity entity, long respawnTime, float cellFloat, float minHeight, float maxHeight) {
//        Grid grid = new Grid((int) (radiusInUnits * 2));
//
//        NoiseGenerator noiseGenerator = new NoiseGenerator();
//        noiseStage(grid, noiseGenerator, 32, 0.6f);
//        noiseStage(grid, noiseGenerator, 16, 0.2f);
//        noiseStage(grid, noiseGenerator, 8, 0.1f);
//        noiseStage(grid, noiseGenerator, 4, 0.1f);
//        noiseStage(grid, noiseGenerator, 1, 0.05f);

//        ArrayList<Location> locs = new ArrayList<>();
//
//        Sphere sp = new Sphere(center.setZ(0).toVector(), radiusInUnits);
//        for (int x = 0; x < 1008; x++) {
//            for (int y = 0; y < 1008; y++) {
//
//                Location searchLoc = world.getRawHeightmap().getWorldLocationFromXY(x, y).setZ(0);
//
//                Sphere i = new Sphere(searchLoc.toVector(), 100);
//                if (sp.overlaps(i)) {
//                    // Is within the region
//                    locs.add(world.getRawHeightmap().getWorldLocationFromXY(x, y));
//                }
//            }
//        }
//
//        int spawnCount = RandomUtil.getRandomNumberBetween(0, 10);
//        for (int i = 0; i < spawnCount; i++) {
//            if (locs.size() > 0) {
//                Location l = RandomUtil.getRandomElementFromList(locs);
//                locs.remove(l);
//
//                GameResourceNode resourceNode = new GameResourceNode();
//                resourceNode.uuid = UUID.randomUUID().toString();
//                resourceNode.location = l;
//                resourceNode.spawnEntity = entity;
//                resourceNode.spawnDelay = TickUtil.MINUTES(respawnTime);
////                resourceNode.realLocation = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightLocationFromLocation(l);
//
//                DedicatedServer.get(ResourceService.class).spawnFromResourceNodePoint(resourceNode);
//
////                try {
////                    DataService.resourceNodes.createOrUpdate(resourceNode);
////                } catch (SQLException throwables) {
////                    throwables.printStackTrace();
////                }
//            }
//        }
    }

    private static void noiseStage(final Grid grid, final NoiseGenerator noiseGenerator, final int radius,
                                   final float modifier) {
        noiseGenerator.setRadius(radius);
        noiseGenerator.setModifier(modifier);
        // Seed ensures randomness, can be saved if you feel the need to
        // generate the same map in the future.
        noiseGenerator.setSeed(Generators.rollSeed());
        noiseGenerator.generate(grid);
    }
}
