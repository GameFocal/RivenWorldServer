package com.gamefocal.rivenworld.game.border;

import com.badlogic.gdx.math.Rectangle;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ChunkBorder {

    private Set<WorldChunk> visitedChunks;
    private Rectangle border;

    public ChunkBorder(Set<WorldChunk> chunks) {
        visitedChunks = new HashSet<>();
        border = null;
        for (WorldChunk chunk : chunks) {
            if (!visitedChunks.contains(chunk)) {
                processChunk(chunk);
            }
        }
    }

    private void processChunk(WorldChunk chunk) {
        Queue<WorldChunk> queue = new LinkedList<>();
        queue.add(chunk);

        while (!queue.isEmpty()) {
            WorldChunk currentChunk = queue.poll();
            if (visitedChunks.contains(currentChunk)) {
                continue;
            }
            visitedChunks.add(currentChunk);

            if (border == null) {
                border = new Rectangle(currentChunk.getBox());
            } else {
                border.merge(currentChunk.getBox());
            }

            for (WorldChunk neighbor : currentChunk.neighbors()) {
                if (neighbor != null && !visitedChunks.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
    }

    public Rectangle getBorder() {
        return border;
    }
}
