package com.gamefocal.rivenworld.game.items.clothes.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.clothes.PlayerWearableItem;

public abstract class PlayerChestItem extends PlayerWearableItem {

    public PlayerChestItem() {
        this.equipTo = EquipmentSlot.BODY;
        this.type = InventoryItemType.NONE;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
