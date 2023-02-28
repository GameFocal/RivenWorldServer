package com.gamefocal.rivenworld.game.items.ammo;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.AmmoInventoryItem;

public class WoodenArrow extends AmmoInventoryItem {

    public WoodenArrow() {
        this.name = "Wooden Arrow";
        this.desc = "";
        this.icon = InventoryDataRow.Wooden_Arrow;
        this.mesh = InventoryDataRow.Wooden_Arrow;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
