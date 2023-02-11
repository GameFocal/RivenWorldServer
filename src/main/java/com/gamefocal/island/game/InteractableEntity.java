package com.gamefocal.island.game;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;

public interface InteractableEntity {
    void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand);
    boolean canInteract(HiveNetConnection netConnection);
    String onFocus(HiveNetConnection connection);
}
