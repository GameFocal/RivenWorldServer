package com.gamefocal.rivenworld.game.generator.basic;

import com.gamefocal.rivenworld.game.World;
import com.gamefocal.rivenworld.game.entites.resources.ground.SmallRockEntity;
import com.gamefocal.rivenworld.game.generator.WorldLayerGenerator;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.models.GameResourceNode;
import com.gamefocal.rivenworld.service.DataService;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.generator.util.Generators;

import java.sql.SQLException;
import java.util.UUID;

public class SmallRockLayer implements WorldLayerGenerator {
    @Override
    public void generateLayer(World world) {

        /*
         * Generate Small Rocks on the Ground
         * */
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

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                float cell = grid.get(x, y);
                float height = world.getRawHeightmap().getHeightFrom2DLocation(new Location(x, y, 0));
                if (cell > .45 && height >= 3500 && RandomUtil.getRandomChance(.01)) {
                    Location worldLoc = world.getRawHeightmap().getWorldLocationFrom2DMap(new Location(x, y, 0));

                    GameResourceNode resourceNode = new GameResourceNode();
                    resourceNode.uuid = UUID.randomUUID().toString();
                    resourceNode.location = worldLoc;
                    resourceNode.spawnEntity = new SmallRockEntity();
                    resourceNode.spawnDelay = 25;

                    try {
                        DataService.resourceNodes.createOrUpdate(resourceNode);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }
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
