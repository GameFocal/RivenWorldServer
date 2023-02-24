package com.gamefocal.rivenworld.entites.chunker;

import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

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
