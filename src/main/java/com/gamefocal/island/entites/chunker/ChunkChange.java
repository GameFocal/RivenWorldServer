package com.gamefocal.island.entites.chunker;

import com.gamefocal.island.game.GameEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lowentry.ue4.library.LowEntry;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class ChunkChange {
    private ChunkChangeType type = ChunkChangeType.BASELINE;

    private JsonObject entity = new JsonObject();

    private Long at = 0L;

    private String hash = "fresh";

    public ChunkChange(ChunkChange prev, ChunkChange next, ChunkChangeType type, JsonObject entityJson) {
        this.type = type;
        this.entity = entityJson;
        this.at = System.currentTimeMillis();
//        this.calcHash();
    }

    public ChunkChange() {
        this.entity = new JsonObject();
        this.at = 0L;
    }

    public void calcHash(String lastHash) {
        this.hash = DigestUtils.md5Hex((lastHash) + this.at + entity.toString());
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("t", this.type.name());
        o.add("e", this.entity);
        return o;
    }

    public String getHash() {
        return hash;
    }

    public ChunkChangeType getType() {
        return type;
    }

    public JsonObject getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object obj) {
        return ((ChunkChange) obj).getHash().equalsIgnoreCase(this.getHash());
    }

    @Override
    public String toString() {
        return this.getHash();
    }
}
