package com.gamefocal.island.game.inventory;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.util.Location;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InventoryItem implements Serializable {

    protected String type;

    protected UUID itemUUID;

    protected float weight = 0.00f;

    protected boolean isStackable = true;

    protected boolean isEquipable = false;

    protected boolean isConsumable = false;

    protected EquipmentSlot equipTo = null;

    protected Map<String, String> meta = new HashMap<>();

    protected float version = 1.0f;

    protected InventoryItemMeta data = new InventoryItemMeta();

    public InventoryItem() {
        this.type = getClass().getName();
    }

    public String hash() {
        StringBuilder b = new StringBuilder();
        b.append(this.getClass().getSimpleName()).append(this.slug());
        for (Map.Entry<String, String> m : this.meta.entrySet()) {
            b.append(m.getKey()).append(m.getValue());
        }
        b.append(this.version);

        return DigestUtils.md5Hex(b.toString());
    }

    public abstract String slug();

    public float getWeight() {
        return weight;
    }

    public boolean isStackable() {
        return isStackable;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public float getVersion() {
        return version;
    }

    public abstract void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action);

    public boolean isEquipable() {
        return isEquipable;
    }

    public boolean isConsumable() {
        return isConsumable;
    }

    public EquipmentSlot getEquipTo() {
        return equipTo;
    }

    public UUID getItemUUID() {
        return itemUUID;
    }
}
