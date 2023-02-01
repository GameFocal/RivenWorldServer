package com.gamefocal.island.game.generator.basic;

import com.gamefocal.island.game.World;
import com.gamefocal.island.game.entites.resources.fruit.BerryBush;
import com.gamefocal.island.game.entites.resources.ground.GroundStickEntity;
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
import java.util.HashMap;
import java.util.UUID;

public class FoodLayer implements WorldLayerGenerator {
    @Override
    public void generateLayer(World world) {
        /*
         * Generate Small Rocks on the Ground
         * */
        float w = world.generator.getHeightmap().getBufferedImage().getWidth();
        float h = world.generator.getHeightmap().getBufferedImage().getHeight();

        Grid grid = new Grid(1008);

//        CellularAutomataGenerator cellularGenerator = new CellularAutomataGenerator();
//        cellularGenerator.setAliveChance(0.5f);
//        cellularGenerator.setRadius(2);
//        cellularGenerator.setBirthLimit(13);
//        cellularGenerator.setDeathLimit(9);
//        cellularGenerator.setIterationsAmount(6);
//        cellularGenerator.generate(grid);

        NoiseGenerator noiseGenerator = new NoiseGenerator();
        noiseStage(grid, noiseGenerator, 32, 0.6f);
        noiseStage(grid, noiseGenerator, 16, 0.2f);
        noiseStage(grid, noiseGenerator, 8, 0.1f);
        noiseStage(grid, noiseGenerator, 4, 0.1f);
        noiseStage(grid, noiseGenerator, 1, 0.05f);

        System.out.println("Generating Rocks...");

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                float cell = grid.get(x, y);
                float height = world.generator.getHeightmap().getHeightFrom2DLocation(new Location(x, y, 0));
                if (cell > .45 && height >= 3500 && RandomUtil.getRandomChance(.001)) {
                    Location worldLoc = world.generator.getHeightmap().getWorldLocationFrom2DMap(new Location(x, y, 0));

                    GameResourceNode resourceNode = new GameResourceNode();
                    resourceNode.uuid = UUID.randomUUID().toString();
                    resourceNode.location = worldLoc;
                    resourceNode.spawnEntity = new BerryBush();
                    resourceNode.spawnDelay = TickUtil.MINUTES(25);

                    try {
                        DataService.resourceNodes.createOrUpdate(resourceNode);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }

        System.out.println("Finished Rocks.");
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
