package com.gamefocal.rivenworld.game.generator;

import com.gamefocal.rivenworld.game.World;
import com.gamefocal.rivenworld.game.heightmap.UnrealHeightmap;

import java.util.Arrays;
import java.util.LinkedList;

public class WorldGenerator {

    private LinkedList<WorldLayerGenerator> layerGenerators = new LinkedList<>();

    private UnrealHeightmap heightmap;

    public WorldGenerator(UnrealHeightmap heightmap, WorldLayerGenerator... generators) {
        this.heightmap = heightmap;
        this.layerGenerators.addAll(Arrays.asList(generators));
    }

    public LinkedList<WorldLayerGenerator> getLayerGenerators() {
        return layerGenerators;
    }

    public UnrealHeightmap getHeightmap() {
        return heightmap;
    }

    public void run(World world) {
        int totalLayers = this.layerGenerators.size();
        int i = 1;
        for (WorldLayerGenerator layerGenerator : this.layerGenerators) {
            System.out.println("[WORLD]: Generating " + layerGenerator.getClass().getSimpleName() + " [" + i + "/" + totalLayers + "]");
            layerGenerator.generateLayer(world);
            i++;
        }
    }
}
