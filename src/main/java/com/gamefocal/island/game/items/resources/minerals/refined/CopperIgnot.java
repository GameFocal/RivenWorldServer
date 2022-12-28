package com.gamefocal.island.game.items.resources.minerals.refined;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.InventoryItem;

public class CopperIgnot extends InventoryItem {
    @Override
    public String slug() {
        return "Copper_Ingot";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}