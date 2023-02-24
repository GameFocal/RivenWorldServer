package com.gamefocal.rivenworld.game;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.net.HiveNetMessage;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.NetworkService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.*;

public abstract class GameEntity<T> implements Serializable {

    public String type = "none";

    public Location location;

    public UUID uuid;

    private HashMap<String, Object> meta = new HashMap<>();

    private boolean isDirty = true;

    private InventoryItem relatedItem;

    private transient ArrayList<HiveNetConnection> loadedBy = new ArrayList<>();

    public GameEntity() {
        this.loadedBy = new ArrayList<>();
    }

    public InventoryItem getRelatedItem() {
        return relatedItem;
    }

    public void setRelatedItem(InventoryItem relatedItem) {
        this.relatedItem = relatedItem;
    }

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

    public GameEntityModel getModel() {
        return DedicatedServer.instance.getWorld().getEntityFromId(this.uuid);
    }

    public String toJsonData() {
        return this.toJsonDataObject().toString();
    }

    public JsonObject toJsonDataObject() {
        JsonObject object = new JsonObject();
        object.addProperty("type", this.type);
        object.addProperty("id", this.uuid.toString());
        object.addProperty("location", this.location.toString());

        Gson g = new Gson();
        JsonElement m = g.toJsonTree(this.meta, HashMap.class);

        object.add("meta", m);
        object.addProperty("hash", (DigestUtils.md5Hex(this.type + this.uuid.toString() + this.location + m.toString())));

        return object;
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
        message.args = new String[]{this.toJsonData()};
        DedicatedServer.get(NetworkService.class).broadcastUdp(message, null);
    }

    public ArrayList<HiveNetConnection> getLoadedBy() {
        return loadedBy;
    }

    public boolean isNetLoaded() {
        return (this.loadedBy.size() > 0);
    }

    public void syncToClients(List<HiveNetConnection> connections) {
        this.onSync();

        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "esync";
        message.args = new String[]{this.toJsonData()};

        for (HiveNetConnection c : connections) {
            c.sendUdp(message.toString());
        }
    }

    public void syncToPlayer(HiveNetConnection c) {
        this.onSync();

        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "esync";
        message.args = new String[]{this.toJsonData()};
        if (c != null) {
            c.sendUdp(message.toString());
        }

        if (this.loadedBy != null) {
            this.loadedBy.add(c);
        }
    }

    public void hideToPlayer(HiveNetConnection c) {
        this.onSync();

        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "ehide";
        message.args = new String[]{this.uuid.toString()};
        c.sendUdp(message.toString());
        if (this.loadedBy != null) {
            this.loadedBy.remove(c);
        }
    }

    public void despawnToPlayer(HiveNetConnection c) {
        this.onSync();

        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "edel";
        message.args = new String[]{this.uuid.toString()};
        c.sendUdp(message.toString());
        if (this.loadedBy != null) {
            this.loadedBy.remove(c);
        }
    }

    public String helpText() {
        return null;
    }
}
