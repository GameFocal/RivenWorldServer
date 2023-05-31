package com.gamefocal.rivenworld.game.inventory;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InventoryItem implements Serializable {

    protected InventoryItemType type = InventoryItemType.NONE;

    protected String name = "A Item";

    protected String desc = "";

    protected Color tint = Color.WHITE;

    protected InventoryDataRow icon = InventoryDataRow.Empty;

    protected InventoryDataRow mesh = InventoryDataRow.Empty;

    protected UUID itemUUID;

    protected float weight = 0.00f;

    protected boolean isStackable = true;

    protected float version = 1.0f;

    protected boolean hasDurability = false;

    protected float durability = 100f;

    protected float maxDurability = 100f;

    protected boolean isEquipable = false;

    protected InventoryItemMeta data = new InventoryItemMeta();

    public EquipmentSlot equipTo = null;

    protected InventoryItemPlacable placable = new InventoryItemPlacable();

    protected ArrayList<String> spawnNames = new ArrayList<>();

    protected float protectionValue = 0;

    public InventoryItem() {
//        this.type = getClass().getName();
    }

    public float getProtectionValue() {
        return protectionValue;
    }

    public String hash() {
        StringBuilder b = new StringBuilder();
        b.append(this.getClass().getSimpleName());
        for (Map.Entry<String, String> m : this.data.getTags().entrySet()) {
            b.append(m.getKey()).append(m.getValue());
        }
        b.append(this.version);

        return DigestUtils.md5Hex(b.toString());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Color getTint() {
        return tint;
    }

    public void setTint(Color tint) {
        this.tint = tint;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setStackable(boolean stackable) {
        isStackable = stackable;
    }

    public void setHasDurability(boolean hasDurability) {
        this.hasDurability = hasDurability;
    }

    public void setDurability(float durability) {
        this.durability = durability;
    }

    public float getMaxDurability() {
        return maxDurability;
    }

    public void setMaxDurability(float maxDurability) {
        this.maxDurability = maxDurability;
    }

    //    public abstract String slug();

    public float getWeight() {
        return weight;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public float getVersion() {
        return version;
    }

    public abstract void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action);

    public UUID getItemUUID() {
        return itemUUID;
    }

    public InventoryItemType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public InventoryDataRow getIcon() {
        return icon;
    }

    public InventoryDataRow getMesh() {
        return mesh;
    }

    public boolean isHasDurability() {
        return hasDurability;
    }

    public float getDurability() {
        return durability;
    }

    public float useDurability(float amt) {
        this.durability -= amt;
        return this.durability;
    }

    public boolean isEquipable() {
        return isEquipable;
    }

    public InventoryItemMeta getData() {
        return data;
    }

    public InventoryItemPlacable getPlacable() {
        return placable;
    }

    public EquipmentSlot getEquipTo() {
        return equipTo;
    }

    public ArrayList<String> getSpawnNames() {
        return spawnNames;
    }

    public boolean onUse(HiveNetConnection connection) {
        return true;
    }

    public InventoryItem attr(String attr) {
        this.data.getAttributes().add(attr);
        return this;
    }

    public InventoryItem tag(String key) {
        this.data.getTags().put(key, "1");
        return this;
    }

    public InventoryItem tag(String key, String data) {
        this.data.getTags().put(key, data);
        return this;
    }

    public void initDurability(int amt) {
        this.maxDurability = amt;
        this.durability = amt;
        this.hasDurability = true;
    }

    public boolean hasTag(String tag) {
        return this.data.getTags().containsKey(tag);
    }

    public boolean tagEquals(String tag, String val) {
        return (this.data.getTags().containsKey(tag) && this.data.getTags().get(tag).equalsIgnoreCase(val));
    }

    public JsonObject toJson() {
        JsonObject i = new JsonObject();
        i.addProperty("type", ((this.type == null) ? InventoryItemType.NONE.name() : this.type.name()));
        i.addProperty("name", this.name);
        i.addProperty("desc", this.desc);
        i.addProperty("icon", this.icon.name());
        i.addProperty("mesh", this.icon.name());
        i.add("attr", DedicatedServer.gson.toJsonTree(this.data.getAttributes(), ArrayList.class));
        i.add("tags", DedicatedServer.gson.toJsonTree(this.data.getTags(), HashMap.class));
        i.addProperty("hasDurability", this.hasDurability);
        i.addProperty("durability", this.durability / this.maxDurability);
        i.addProperty("className", this.getClass().getSimpleName());
        i.add("placable", DedicatedServer.gson.toJsonTree(this.placable, InventoryItemPlacable.class));
        i.addProperty("tint", this.tint.toString());
        return i;
    }
}
