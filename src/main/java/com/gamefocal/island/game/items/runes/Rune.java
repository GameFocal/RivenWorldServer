package com.gamefocal.island.game.items.runes;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.InventoryItem;

public abstract class Rune extends InventoryItem {

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        if(action == InteractAction.USE) {
            // Use the Rune logic here?
        }
    }
}
