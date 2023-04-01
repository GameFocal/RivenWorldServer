package com.gamefocal.rivenworld.game.entites.special;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.weapons.Torch;

import java.util.concurrent.TimeUnit;

public class HandTorchInGround extends GameEntity<HandTorchInGround> implements TickEntity, InteractableEntity {

    private Long placed = -1L;

    public HandTorchInGround() {
        this.type = "TorchNet";
    }

    @Override
    public void onSpawn() {
        this.placed = System.currentTimeMillis();
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        if (this.placed > 0) {
            if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - this.placed) > 15) {
                System.out.println("Despawn by tick");
                DedicatedServer.instance.getWorld().despawn(this.uuid);
            }
        }
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            DedicatedServer.instance.getWorld().despawn(this.uuid);
            connection.getPlayer().inventory.add(new InventoryStack(new Torch(), 1));
        }
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Pickup Torch";
    }
}
