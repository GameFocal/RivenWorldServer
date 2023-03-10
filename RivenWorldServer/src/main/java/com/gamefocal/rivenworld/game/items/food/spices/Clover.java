package com.gamefocal.rivenworld.game.items.food.spices;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Clover extends InventoryItem {

    public Clover() {
        this.icon = InventoryDataRow.Clover_plant;
        this.mesh = InventoryDataRow.Clover_plant;
        this.name = "Clover";
        this.desc = "Can be used in recipes";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
