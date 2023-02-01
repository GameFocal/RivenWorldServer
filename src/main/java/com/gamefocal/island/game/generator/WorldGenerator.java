package com.gamefocal.island.game.generator;

import com.gamefocal.island.game.World;

import java.util.Arrays;
import java.util.LinkedList;

public class WorldGenerator {

    private LinkedList<WorldLayerGenerator> layerGenerators = new LinkedList<>();

    private Heightmap heightmap;

    public WorldGenerator(Heightmap heightmap, WorldLayerGenerator... generators) {
        this.heightmap = heightmap;
        this.layerGenerators.addAll(Arrays.asList(generators));
    }

    public LinkedList<WorldLayerGenerator> getLayerGenerators() {
        return layerGenerators;
    }

    public Heightmap getHeightmap() {
        return heightmap;
    }

    public void run(World world) {
        int totalLayers = this.layerGenerators.size();
        int i = 1;
        for (WorldLayerGenerator layerGenerator : this.layerGenerators) {
            System.out.println("Generating " + layerGenerator.getClass().getSimpleName() + " [" + i + "/" + totalLayers + "] " + (Math.round((i/totalLayers)*100)) + "% Complete");
            layerGenerator.generateLayer(world);
            i++;
        }
    }
}
