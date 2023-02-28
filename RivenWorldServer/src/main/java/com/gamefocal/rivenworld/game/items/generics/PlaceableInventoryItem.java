package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;

public abstract class PlaceableInventoryItem<S extends PlaceableInventoryItem> extends InventoryItem {

    public PlaceableInventoryItem() {
        this.isEquipable = true;
        this.equipTo = EquipmentSlot.PRIMARY;
        this.type = InventoryItemType.PLACABLE;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
    }

    public abstract GameEntity spawnItem();
}
