package com.gamefocal.rivenworld.game.items.ammo;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.AmmoInventoryItem;

public class IronArrow extends AmmoInventoryItem {

    public IronArrow() {
        this.name = "Iron Arrow";
        this.desc = "A stick with a sharp end that can kill something";
        this.icon = InventoryDataRow.Iron_Arrow;
        this.mesh = InventoryDataRow.Iron_Arrow;
    }

    @Override
    public float damage() {
        return 35;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
