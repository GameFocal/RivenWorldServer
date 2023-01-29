package com.gamefocal.island.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldChunk {

    public ConcurrentLinkedQueue<UUID> entites = new ConcurrentLinkedQueue<>();
    private Vector2 location;
    private BoundingBox boundingBox;
    private Long version = 0L;
    private boolean isDirty = false;

    public WorldChunk(Vector2 location) {
        this.location = location;

        Vector3 lowerLeft = new Vector3(location.x * 100, location.y * 100, -9999999);
        Vector3 upperRight = new Vector3(lowerLeft.x + (100 * 20), lowerLeft.y + (100 * 20), 9999999);

        this.boundingBox = new BoundingBox(lowerLeft,upperRight);
    }

    public void trackEntity(UUID uuid) {
        this.entites.add(uuid);
    }

    public void untrackEntity(UUID uuid) {
        this.entites.remove(uuid);
    }

    public boolean entityInside(UUID uuid) {
        return this.entites.contains(uuid);
    }

    public ConcurrentLinkedQueue<UUID> getEntites() {
        return entites;
    }

    public Vector2 getLocation() {
        return location;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public Long getVersion() {
        return version;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void update() {
        this.version = System.currentTimeMillis();
        this.isDirty = true;
    }

    public String getHash() {

        StringBuilder entityHash = new StringBuilder();


        return "NONE";
    }
}
