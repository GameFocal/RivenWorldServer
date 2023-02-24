package com.gamefocal.rivenworld.game.items.resources.minerals.refined;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;

public class SteelIgnot extends InventoryItem {
    @Override
    public String slug() {
        return "Steel_Ingot";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}