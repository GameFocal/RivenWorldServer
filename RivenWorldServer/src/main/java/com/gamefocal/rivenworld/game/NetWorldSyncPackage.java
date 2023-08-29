package com.gamefocal.rivenworld.game;

import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class NetWorldSyncPackage {

    private LinkedList<JsonObject> sync = new LinkedList<>();
    private LinkedList<UUID> desync = new LinkedList<>();
    private HashMap<Location, String> chunkHashes = new HashMap<>();

    public void addSyncObject(JsonObject object) {
        this.sync.add(object);
    }

    public void addDeSyncUUID(UUID uuid) {
        this.desync.add(uuid);
    }

    public void addChunkHash(WorldChunk chunk) {
        this.chunkHashes.put(chunk.getChunkCords(), String.valueOf(chunk.getVersion()));
    }

    public boolean hasData() {
        return (this.operationCount() > 0);
    }

    public int operationCount() {
        return (sync.size() + desync.size());
    }

    public JsonObject getJson() {
        JsonObject p = new JsonObject();

        JsonArray sync = new JsonArray();
        for (JsonObject o : this.sync) {
            sync.add(o);
        }

        JsonArray desync = new JsonArray();
        for (UUID u : this.desync) {
            desync.add(u.toString());
        }

        JsonObject cc = new JsonObject();
        for (Map.Entry<Location, String> m : this.chunkHashes.entrySet()) {
            cc.addProperty(m.getKey().toString(), m.getValue());
        }

        p.add("sync", sync);
        p.add("desync", desync);
        p.add("chunks", cc);

        return p;
    }

}
