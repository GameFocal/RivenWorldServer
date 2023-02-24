package com.gamefocal.rivenworld.game;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;

public interface InteractableEntity {
    void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand);
    boolean canInteract(HiveNetConnection netConnection);
    String onFocus(HiveNetConnection connection);
}
