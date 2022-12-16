package com.gamefocal.island.game.items.ammo;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.AmmoInventoryItem;

public class WoodenArrow extends AmmoInventoryItem {
    @Override
    public String slug() {
        return "Wooden_Arrow";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
