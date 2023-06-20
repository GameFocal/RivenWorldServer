package com.gamefocal.rivenworld.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.LinkedList;
import java.util.UUID;

public class NetWorldSyncPackage {

    private LinkedList<JsonObject> sync = new LinkedList<>();
    private LinkedList<UUID> desync = new LinkedList<>();

    public void addSyncObject(JsonObject object) {
        this.sync.add(object);
    }

    public void addDeSyncUUID(UUID uuid) {
        this.desync.add(uuid);
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

        p.add("sync", sync);
        p.add("desync", desync);

        return p;
    }

}
