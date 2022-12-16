package com.gamefocal.island.game.items.weapons;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.ToolInventoryItem;

public class Bow extends ToolInventoryItem {
    @Override
    public String slug() {
        return "Basic_Bow";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
