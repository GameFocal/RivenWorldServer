package com.gamefocal.island.game.entites.resources;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.DestructibleEntity;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;

public abstract class ResourceNodeEntity<A> extends GameEntity<A> implements InteractableEntity {

    public float health = 100f;

    public float maxHealth = 100f;

    public InventoryStack[] drops = new InventoryStack[0];

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.HIT) {
            // Hits the rock node

            System.out.println("HIT " + getClass().getSimpleName() + " NODE");

        }
    }
}
