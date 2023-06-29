package com.gamefocal.rivenworld.game.items.food.spices;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class Camomile extends InventoryItem {

    public Camomile() {
        this.icon = InventoryDataRow.Camomile;
        this.mesh = InventoryDataRow.Camomile;
        this.name = "Chamomile Flower";
        this.desc = "Can be used in recipes";
        this.spawnNames.add("ch");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
