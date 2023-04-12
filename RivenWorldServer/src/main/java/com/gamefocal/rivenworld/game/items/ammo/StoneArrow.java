package com.gamefocal.rivenworld.game.items.ammo;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.AmmoInventoryItem;

public class StoneArrow extends AmmoInventoryItem {

    public StoneArrow() {
        this.name = "Stone Arrow";
        this.desc = "A stick with a sharp end that can kill something";
        this.icon = InventoryDataRow.Stone_Arrow;
        this.mesh = InventoryDataRow.Stone_Arrow;
    }

    @Override
    public float damage() {
        return 20;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
