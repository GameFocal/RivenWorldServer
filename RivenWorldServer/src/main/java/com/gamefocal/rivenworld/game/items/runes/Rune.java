package com.gamefocal.rivenworld.game.items.runes;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;

public abstract class Rune extends InventoryItem {

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        if(action == InteractAction.USE) {
            // Use the Rune logic here?
        }
    }
}
