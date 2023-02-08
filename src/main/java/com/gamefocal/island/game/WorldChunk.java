package com.gamefocal.island.game;

import com.badlogic.gdx.math.Rectangle;
import com.gamefocal.island.game.util.Location;

public class WorldChunk {

    World world;
    float x;
    float y;
    Location start;
    Rectangle box;
    Location center;

    public WorldChunk(World world, float x, float y) {
        this.world = world;
        this.x = x;
        this.y = y;

        float cellSize = 2400;

        float worldX = (this.x * cellSize);
        float worldY = (this.y * cellSize);

        Location realLoc = this.world.fromZeroBasedCords(new Location(worldX, worldY, 0));

        this.start = realLoc;
        this.center = start.cpy().addX((this.world.getChunkSize() * 100) / 2f).addY((this.world.getChunkSize() * 100) / 2f);
        this.box = new Rectangle(this.start.getX(), this.start.getY(), this.world.getChunkSize() * 100, this.world.getChunkSize() * 100);
    }

    public Location getChunkCords() {
        return new Location(this.x, this.y, 0);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Location getStart() {
        return start;
    }

    public Location getCenter() {
        return this.center;
    }

    public Rectangle getBox() {
        return box;
    }

    public WorldChunk north() {
        return this.world.getChunk(this.x, this.y + 1);
    }

    public WorldChunk east() {
        return this.world.getChunk(this.x + 1, this.y);
    }

    public WorldChunk south() {
        return this.world.getChunk(this.x, this.y - 1);
    }

    public WorldChunk west() {
        return this.world.getChunk(this.x - 1, this.y);
    }

    public WorldChunk[] neighbors() {
        return new WorldChunk[]{
                this.north(),
                this.east(),
                this.south(),
                this.west()
        };
    }

    @Override
    public String toString() {
        return this.getChunkCords().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getChunkCords().toString().equalsIgnoreCase(obj.toString());
    }
}
