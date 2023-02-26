package com.gamefocal.rivenworld.game.entites.resources;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;

public abstract class ResourceNodeEntity<A> extends GameEntity<A> implements InteractableEntity {

    public float health = 100f;

    public float maxHealth = 100f;

//    public InventoryStack[] drops = new InventoryStack[0];

    public abstract InventoryStack[] drops();

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return null;
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
    }
}
