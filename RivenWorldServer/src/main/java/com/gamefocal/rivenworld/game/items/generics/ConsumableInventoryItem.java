package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.combat.NetHitResult;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;

public abstract class ConsumableInventoryItem extends InventoryItem implements UsableInventoryItem {

    public ConsumableInventoryItem() {
        this.type = InventoryItemType.CONSUMABLE;
    }

    public abstract float onConsume(HiveNetConnection connection);

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        // Do Nothing
    }

    @Override
    public String inHandTip(HiveNetConnection connection, NetHitResult hitResult) {
        return "[e] Consume";
    }

    @Override
    public void onUse(HiveNetConnection connection, NetHitResult hitResult, InteractAction action, InventoryStack inHand) {
        // TODO: ZP, eat the food
    }
}
