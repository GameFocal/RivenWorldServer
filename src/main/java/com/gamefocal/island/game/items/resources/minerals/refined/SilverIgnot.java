package com.gamefocal.island.game.items.resources.minerals.refined;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.InventoryItem;

public class SilverIgnot extends InventoryItem {
    @Override
    public String slug() {
        return "Silver_Ingot";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
