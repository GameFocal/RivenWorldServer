package com.gamefocal.rivenworld.game.generator.basic;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.entites.resources.ground.SmallRockEntity;
import com.gamefocal.rivenworld.game.generator.WorldLayerGenerator;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.RandomUtil;
import com.gamefocal.rivenworld.game.world.World;
import com.gamefocal.rivenworld.game.world.WorldMetaData;
import com.gamefocal.rivenworld.models.GameResourceNode;
import com.gamefocal.rivenworld.service.ResourceService;
import com.github.czyzby.noise4j.map.Grid;
import com.github.czyzby.noise4j.map.generator.noise.NoiseGenerator;
import com.github.czyzby.noise4j.map.generator.util.Generators;

import java.util.UUID;

public class SmallRockLayer implements WorldLayerGenerator {
    private static void noiseStage(final Grid grid, final NoiseGenerator noiseGenerator, final int radius,
                                   final float modifier) {
        noiseGenerator.setRadius(radius);
        noiseGenerator.setModifier(modifier);
        // Seed ensures randomness, can be saved if you feel the need to
        // generate the same map in the future.
        noiseGenerator.setSeed(Generators.rollSeed());
        noiseGenerator.generate(grid);
    }

    @Override
    public void generateLayer(World world) {

        /*
         * Generate Small Rocks on the Ground
         * */
        Grid grid = new Grid((201753 / 100));

        NoiseGenerator noiseGenerator = new NoiseGenerator();
        noiseStage(grid, noiseGenerator, 32, 0.6f);
        noiseStage(grid, noiseGenerator, 16, 0.2f);
        noiseStage(grid, noiseGenerator, 8, 0.1f);
        noiseStage(grid, noiseGenerator, 4, 0.1f);
        noiseStage(grid, noiseGenerator, 1, 0.05f);

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                float cell = grid.get(x, y);
                float height = world.getRawHeightmap().getHeightValue(x, y);
                if (cell > .55 && height >= 3500 && RandomUtil.getRandomChance(.01)) {
                    Location worldLoc = world.getRawHeightmap().getWorldLocationFromXY(x, y);

                    WorldMetaData metaData = world.getRawHeightmap().getMetaDataFromXY(x, y);

                    if (metaData.getForest() == 1) {
                        GameResourceNode resourceNode = new GameResourceNode();
                        resourceNode.uuid = UUID.randomUUID().toString();
                        resourceNode.location = worldLoc;
                        resourceNode.spawnEntity = new SmallRockEntity();
                        resourceNode.spawnDelay = 25;

                        DedicatedServer.get(ResourceService.class).spawnFromResourceNodePoint(resourceNode);
                    }
                }
            }
        }
    }
}
