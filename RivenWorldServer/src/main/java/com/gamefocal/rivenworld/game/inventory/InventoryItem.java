package com.gamefocal.rivenworld.game.inventory;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryIcon;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryMesh;
import com.gamefocal.rivenworld.game.inventory.equipment.EquipmentSlot;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InventoryItem implements Serializable {

//    protected String type;

    protected InventoryIcon icon;

    protected InventoryMesh mesh;

    protected UUID itemUUID;

    protected float weight = 0.00f;

    protected boolean isStackable = true;

    protected boolean canEquip = false;

    protected float version = 1.0f;

    protected InventoryItemMeta data = new InventoryItemMeta();

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

    public abstract InventoryIcon icon();

    public abstract InventoryMesh mesh();

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
}
