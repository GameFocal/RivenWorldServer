package com.gamefocal.rivenworld.game.world;

import com.gamefocal.rivenworld.game.util.Location;

public class WorldMetaData {

    private Location tileLocation;
    private byte biome;
    private byte forest;
    private float height;

    public WorldMetaData(Location tileLocation, byte biome, byte forest, float height) {
        this.tileLocation = tileLocation;
        this.biome = biome;
        this.forest = forest;
        this.height = height;
    }

    public Location getTileLocation() {
        return tileLocation;
    }

    public byte getBiome() {
        return biome;
    }

    public byte getForest() {
        return forest;
    }

    public float getHeight() {
        return height;
    }
}
