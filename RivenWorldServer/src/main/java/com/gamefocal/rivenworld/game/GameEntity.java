package com.gamefocal.rivenworld.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.entites.net.HiveNetMessage;
import com.gamefocal.rivenworld.game.entites.NetworkUpdateFrequency;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.NetworkService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.*;

public abstract class GameEntity<T> implements Serializable {

    public boolean useWorldSyncThread = true;
    public boolean useSpacialLoading = true;
    public int spacialLOD = 0;
    public String type = "none";
    public Location location;
    public UUID uuid;
    protected HashMap<String, Object> meta = new HashMap<>();
    protected NetworkUpdateFrequency updateFrequency = NetworkUpdateFrequency.NORMAL;
    protected long lastNetworkUpdate = 0L;
    private Vector2 dimensions = new Vector2(25, 50);
    private boolean isDirty = true;
    private boolean hasCollision = true;
    private String chunkHash = "NA";
    private InventoryItem relatedItem;
    private transient ArrayList<HiveNetConnection> loadedBy = new ArrayList<>();

    public GameEntity() {
        this.loadedBy = new ArrayList<>();
    }

    public NetworkUpdateFrequency getUpdateFrequency() {
        return updateFrequency;
    }

    public long getLastNetworkUpdate() {
        return lastNetworkUpdate;
    }

    public void setLastNetworkUpdate(long lastNetworkUpdate) {
        this.lastNetworkUpdate = lastNetworkUpdate;
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

    public boolean hasMeta(String path) {
        return this.meta.containsKey(path);
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

    public void calcChunkHash() {
        WorldChunk chunk = this.getChunk();
        if (chunk != null) {
            this.chunkHash = DigestUtils.md5Hex(chunk.getChunkCords().toString());
        }
    }

    public boolean hasMovedChunks() {
        WorldChunk c = this.getChunk();
        if (c != null) {
            return !this.chunkHash.equalsIgnoreCase(DigestUtils.md5Hex(c.getChunkCords().toString()));
        }

        return false;
    }

    public String getChunkHash() {
        return chunkHash;
    }

    public boolean isHasCollision() {
        return hasCollision;
    }

    public void setHasCollision(boolean hasCollision) {
        this.hasCollision = hasCollision;
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
        object.addProperty("hash", this.entityHash());

        return object;
    }

    public void onHash(StringBuilder builder) {

    }

    public String entityHash() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.type).append(this.meta.toString()).append(this.location.toString()).append(this.isDirty);

        this.onHash(builder);

        return DigestUtils.md5Hex(builder.toString());
    }

    public void despawn() {
        this.onDespawn();
        HiveNetMessage message = new HiveNetMessage();
        message.cmd = "edel";
        message.args = new String[]{this.uuid.toString()};

        for (HiveNetConnection subs : DedicatedServer.get(PlayerService.class).players.values()) {
            subs.sendTcp(message.toString());
        }

//        DedicatedServer.get(NetworkService.class).broadcastUdp(message, null);
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

    public WorldChunk getChunk() {
        return DedicatedServer.instance.getWorld().getChunk(this.location);
    }

    public void forceUpdate() {
        this.getChunk().updateEntity(this.getModel());
    }

    public String helpText(HiveNetConnection connection) {
        return null;
    }

    public void LOD0() {
        this.configureSpacialLOD(0);
    }

    public void LOD1() {
        this.configureSpacialLOD(1);
    }

    public void LOD2() {
        this.configureSpacialLOD(2);
    }

    public void LOD3() {
        this.configureSpacialLOD(3);
    }

    public void LOD4() {
        this.configureSpacialLOD(4);
    }

    public Vector2 getDimensions() {
        return dimensions;
    }

    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 50);
    }

    public void configureSpacialLOD(int lod) {
        this.useSpacialLoading = true;
        this.spacialLOD = lod;
    }

    public void configureAlwaysLoaded() {
        this.useSpacialLoading = false;
        this.spacialLOD = 0;
    }

    public void onLoad() {

    }

    public void onSave() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameEntity)) return false;
        GameEntity<?> entity = (GameEntity<?>) o;
        return Objects.equal(uuid, entity.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
