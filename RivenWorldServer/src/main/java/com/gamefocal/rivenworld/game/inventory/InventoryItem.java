package com.gamefocal.rivenworld.game.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InventoryItem implements Serializable {

    protected InventoryItemType type;

    protected String name = "A Item";

    protected String desc = "";

    protected InventoryDataRow icon = InventoryDataRow.Empty;

    protected InventoryDataRow mesh = InventoryDataRow.Empty;

    protected UUID itemUUID;

    protected float weight = 0.00f;

    protected boolean isStackable = true;

    protected float version = 1.0f;

    protected boolean hasDurability = false;

    protected float durability = 100f;

    protected boolean isEquipable = false;

    protected InventoryItemMeta data = new InventoryItemMeta();

    public EquipmentSlot equipTo = null;

    protected InventoryItemPlacable placable = new InventoryItemPlacable();

    protected ArrayList<String> spawnNames = new ArrayList<>();

    public InventoryItem() {
//        this.type = getClass().getName();
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

    public JsonObject toJson() {
        JsonObject i = new JsonObject();
        i.addProperty("name", this.name);
        i.addProperty("desc", this.desc);
        i.addProperty("icon", this.icon.name());
        i.addProperty("mesh", this.icon.name());
        i.addProperty("attr", DedicatedServer.gson.toJson(this.data.getAttributes(), ArrayList.class));
        i.addProperty("tags", DedicatedServer.gson.toJson(this.data.getTags(), HashMap.class));
        i.addProperty("hasDurability", this.hasDurability);
        i.addProperty("durability", this.durability);
        i.add("placable", DedicatedServer.gson.toJsonTree(this.placable, InventoryItemPlacable.class));

        return i;
    }
}
