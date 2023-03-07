package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.combat.NetHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;

public interface UsableInventoryItem {
    String inHandTip(HiveNetConnection connection, NetHitResult hitResult);

    void onUse(HiveNetConnection connection, NetHitResult hitResult, InteractAction action, InventoryStack inHand);
}
