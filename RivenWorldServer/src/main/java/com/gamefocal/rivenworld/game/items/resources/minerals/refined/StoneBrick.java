package com.gamefocal.rivenworld.game.items.resources.minerals.refined;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class StoneBrick extends InventoryItem {

    public StoneBrick() {
        this.icon = InventoryDataRow.Stone_Brick;
        this.mesh = InventoryDataRow.Stone_Brick;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
