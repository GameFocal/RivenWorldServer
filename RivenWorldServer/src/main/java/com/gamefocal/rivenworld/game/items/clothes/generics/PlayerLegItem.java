package com.gamefocal.rivenworld.game.items.clothes.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.clothes.PlayerWearableItem;

public abstract class PlayerLegItem extends PlayerWearableItem {

    public PlayerLegItem() {
        this.equipTo = EquipmentSlot.LEGS;
        this.type = InventoryItemType.NONE;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
