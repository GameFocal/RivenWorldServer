package com.gamefocal.island.game.entites.resources.ground;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.resources.wood.WoodStick;

public class GroundStickEntity extends GameEntity<GroundStickEntity> implements InteractableEntity {

    public GroundStickEntity() {
        this.type = "ground-stick";
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            // Pickup the Stick

            InventoryStack stack = new InventoryStack(new WoodStick());
            connection.displayItemAdded(stack);
            connection.getPlayer().inventory.add(stack);
            DedicatedServer.instance.getWorld().despawn(this.uuid);
        }
    }
}