package com.gamefocal.island.game.generator.basic;

import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.World;
import com.gamefocal.island.game.entites.resources.nodes.RockNode;
import com.gamefocal.island.game.generator.WorldLayerGenerator;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.game.util.RandomUtil;
import com.gamefocal.island.game.util.TickUtil;
import com.gamefocal.island.models.GameResourceNode;
import com.gamefocal.island.service.DataService;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.generator.util.Generators;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class MineralLayer implements WorldLayerGenerator {
    @Override
    public void generateLayer(World world) {

        ArrayList<String> rocks = new ArrayList<>();
        rocks.add("116484.18,108406.71,7280.89,0.0,0.0,106.91916");
        rocks.add("116484.18,108406.71,7280.89,0.0,0.0,106.91916");
        rocks.add("120720.14,107425.445,7522.6743,0.0,0.0,23.02552");
        rocks.add("125739.44,106282.32,7661.929,0.0,0.0,-12.943298");
        rocks.add("128839.08,109387.75,7503.728,0.0,0.0,49.66327");

        for (String s : rocks) {
            generateEntity(world, Location.fromString(s), 500, .05f, new RockNode(), 15, .45f, 4500, 55000);
        }

        // TODO: Gold

        // TODO: Copper

        // TODO: Dirt
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

        ArrayList<Location> locs = new ArrayList<>();

        Sphere sp = new Sphere(center.setZ(0).toVector(), radiusInUnits);
        for (int x = 0; x < 1008; x++) {
            for (int y = 0; y < 1008; y++) {

                Location searchLoc = world.generator.getHeightmap().getWorldLocationFrom2DMap(new Location(x,y,0)).setZ(0);

                Sphere i = new Sphere(searchLoc.toVector(), 100);
                if (sp.overlaps(i)) {
                    // Is within the region
                    locs.add(world.generator.getHeightmap().getWorldLocationFrom2DMap(new Location(x, y, 0)));
                }
            }
        }

        int spawnCount = RandomUtil.getRandomNumberBetween(0, 3);
        for (int i = 0; i < spawnCount; i++) {
            if(locs.size() > 0) {
                Location l = RandomUtil.getRandomElementFromList(locs);
                locs.remove(l);

                GameResourceNode resourceNode = new GameResourceNode();
                resourceNode.uuid = UUID.randomUUID().toString();
                resourceNode.location = l;
                resourceNode.spawnEntity = entity;
                resourceNode.spawnDelay = TickUtil.MINUTES(respawnTime);

                try {
                    DataService.resourceNodes.createOrUpdate(resourceNode);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                System.err.println("No Loc to choose from");
            }
        }

//        for (int x = 0; x < grid.getWidth(); x++) {
//            for (int y = 0; y < grid.getHeight(); y++) {
//                float cell = grid.get(x, y);
//                float height = world.generator.getHeightmap().getHeightFrom2DLocation(new Location(x, y, 0));
//                if (cell > cellFloat && height >= minHeight && height <= maxHeight && RandomUtil.getRandomChance(chance)) {
//                    Location worldLoc = world.generator.getHeightmap().getWorldLocationFrom2DMap(new Location(x, y, 0));
//
//                    GameResourceNode resourceNode = new GameResourceNode();
//                    resourceNode.uuid = UUID.randomUUID().toString();
//                    resourceNode.location = worldLoc;
//                    resourceNode.spawnEntity = entity;
//                    resourceNode.spawnDelay = TickUtil.MINUTES(respawnTime);
//
//                    try {
//                        DataService.resourceNodes.createOrUpdate(resourceNode);
//                    } catch (SQLException throwables) {
//                        throwables.printStackTrace();
//                    }
//                }
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
