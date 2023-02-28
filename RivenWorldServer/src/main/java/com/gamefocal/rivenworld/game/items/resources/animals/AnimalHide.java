package com.gamefocal.rivenworld.game.items.resources.animals;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class AnimalHide extends InventoryItem {

    public AnimalHide() {
        this.icon = InventoryDataRow.Hide;
        this.mesh = InventoryDataRow.Hide;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
