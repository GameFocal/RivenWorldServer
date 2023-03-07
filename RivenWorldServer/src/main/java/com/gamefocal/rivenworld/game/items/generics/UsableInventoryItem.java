package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.combat.NetHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ray.HitResult;

public interface UsableInventoryItem {
    String inHandTip(HiveNetConnection connection, NetHitResult hitResult);

    boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand);
}
