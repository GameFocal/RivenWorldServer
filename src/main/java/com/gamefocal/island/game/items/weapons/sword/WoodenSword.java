package com.gamefocal.island.game.items.weapons.sword;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.ToolInventoryItem;

public class WoodenSword extends ToolInventoryItem {
    @Override
    public String slug() {
        return "Wooden_Sword";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 0;
    }

    @Override
    public float block() {
        return 0;
    }
}
