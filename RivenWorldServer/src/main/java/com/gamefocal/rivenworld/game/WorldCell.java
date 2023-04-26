package com.gamefocal.rivenworld.game;

import com.gamefocal.rivenworld.models.GameEntityModel;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WorldCell {

    private WorldChunk chunk;
    private ConcurrentHashMap<UUID, GameEntityModel> entites = new ConcurrentHashMap<>();
    private int x;
    private int y;
    private int z;
    private boolean isBlocked = false;

    public WorldCell(WorldChunk chunk, int x, int y, int z) {
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WorldChunk getChunk() {
        return chunk;
    }

    public ConcurrentHashMap<UUID, GameEntityModel> getEntites() {
        return entites;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
