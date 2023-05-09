package com.gamefocal.rivenworld.game.border;

import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.HashSet;
import java.util.Set;

public class ChunkBorderFinder {
    private Set<WorldChunk> chunks;

    public ChunkBorderFinder(Set<WorldChunk> chunks) {
        this.chunks = chunks;
    }

    public Set<Location> findBorderPoints() {
        Set<Location> borderPoints = new HashSet<>();

        for (WorldChunk chunk : chunks) {
            WorldChunk[] neighbors = chunk.neighbors();

            for (int i = 0; i < neighbors.length; i++) {
                if (!chunks.contains(neighbors[i])) {
                    // If the neighbor is not part of the chunks set, it is a border
                    borderPoints.add(getEdgePoints(chunk, i));
                }
            }
        }

        return borderPoints;
    }

    private Location getEdgePoints(WorldChunk chunk, int direction) {
        Location start = chunk.getStart();
        float chunkSize = chunk.getWorld().getChunkSize() * 100;

        switch (direction) {
            case 0: // North
                return start.cpy().addX(chunkSize).addY(chunkSize);
            case 1: // East
                return start.cpy().addX(chunkSize).addY(chunkSize / 2);
            case 2: // South
                return start.cpy().addX(chunkSize / 2);
            case 3: // West
                return start.cpy().addY(chunkSize / 2);
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }
}
