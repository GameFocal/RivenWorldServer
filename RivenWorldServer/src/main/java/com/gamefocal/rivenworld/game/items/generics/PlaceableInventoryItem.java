package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;

public abstract class PlaceableInventoryItem<S extends PlaceableInventoryItem> extends InventoryItem implements EquipmentItem {

    public PlaceableInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.type = InventoryItemType.PLACABLE;

        GameEntity spawn = this.spawnItem();
        if (spawn != null && DestructibleEntity.class.isAssignableFrom(spawn.getClass())) {
            this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Hitpoints: " + ((DestructibleEntity<?>) spawn).getMaxHealth());
        }
    }

    @Override
    public boolean canEquip(HiveNetConnection connection) {
        return true;
    }

    @Override
    public String toSocket() {
        return "Primary";
    }

    @Override
    public void onEquip(HiveNetConnection connection) {

    }

    @Override
    public void onUnequipped(HiveNetConnection connection) {

    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
    }

    public abstract GameEntity spawnItem();
}
