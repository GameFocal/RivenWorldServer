package com.gamefocal.rivenworld.game.items.resources.minerals.refined;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class SilverIgnot extends InventoryItem {

    public SilverIgnot() {
        this.icon = InventoryDataRow.Silver_Ingot;
        this.mesh = InventoryDataRow.Silver_Ingot;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
