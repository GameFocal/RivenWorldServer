package com.gamefocal.island.game;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.NetworkService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class GameEntity<T> implements Serializable {

    public String type = "none";

    public Location location;

    public UUID uuid;

    private HashMap<String, Object> meta = new HashMap<>();

    private boolean isDirty = true;

    public void setMeta(String path, String val) {
        meta.put(path, val);
    }

    public void setMeta(String path, Number val) {
        meta.put(path, val);
    }

    public void setMeta(String path, boolean val) {
        meta.put(path, val);
    }

    public String getMetaString(String path) {
        return (String) this.meta.get(path);
    }

    public Integer getMetaInt(String path) {
        return (Integer) this.meta.get(path);
    }

    public Float getMetaFloat(String path) {
        return (Float) this.meta.get(path);
    }

    public Double getMetaDouble(String path) {
        return (Double) this.meta.get(path);
    }

    public Boolean getMetaBool(String path) {
        return (Boolean) this.meta.get(path);
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void onSync() {
        // Can override this to update things on updates
    }

    public abstract void onSpawn();

    public abstract void onDespawn();

    public abstract void onTick();

    public String toJsonData() {
        JsonObject object = new JsonObject();
        object.addProperty("type", this.type);
        object.addProperty("id", this.uuid.toString());
        object.addProperty("location", this.location.toString());

        Gson g = new Gson();
        JsonElement m = g.toJsonTree(this.meta, HashMap.class);

        object.add("meta", m);

        return object.toString();
    }

    public void despawn() {
        this.onDespawn();
        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "edel";
        message.args = new String[]{this.uuid.toString()};
        DedicatedServer.get(NetworkService.class).broadcastUdp(message, null);
    }

    public void syncAll() {
        this.onSync();

        // Broadcast the sync to all clients
        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "esync";
        message.args = new String[]{Base64.getEncoder().encodeToString(this.toJsonData().getBytes(StandardCharsets.UTF_8))};
        DedicatedServer.get(NetworkService.class).broadcastUdp(message, null);
    }

    public void syncToClients(List<HiveNetConnection> connections) {
        this.onSync();

        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "esync";
        message.args = new String[]{Base64.getEncoder().encodeToString(this.toJsonData().getBytes(StandardCharsets.UTF_8))};

        for (HiveNetConnection c : connections) {
            c.sendUdp(c.toString());
        }
    }

    public void syncToPlayer(HiveNetConnection c) {
        this.onSync();

        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "esync";
        message.args = new String[]{Base64.getEncoder().encodeToString(this.toJsonData().getBytes(StandardCharsets.UTF_8))};
        c.sendUdp(c.toString());
    }
}
