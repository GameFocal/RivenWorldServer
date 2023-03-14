package com.gamefocal.rivenworld.game.entites.resources.ground;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.GoldOre;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.IronOre;

public class IronRockEntity extends GameEntity<IronRockEntity> implements InteractableEntity {

    public IronRockEntity() {
        this.type = "iron-rock";
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
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Pickup Iron Ore";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            // Pickup the Stick

            InventoryStack stack = new InventoryStack(new IronOre());
            connection.displayItemAdded(stack);
            connection.getPlayer().inventory.add(stack);
            connection.updatePlayerInventory();
            DedicatedServer.instance.getWorld().despawn(this.uuid);
        }
    }
}
