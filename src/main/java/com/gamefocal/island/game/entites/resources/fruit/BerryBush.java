package com.gamefocal.island.game.entites.resources.fruit;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.GameEntity;
import com.gamefocal.island.game.InteractableEntity;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.food.consumable.Blueberry;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.game.util.RandomUtil;

public class BerryBush extends GameEntity<BerryBush> implements InteractableEntity {

    public boolean hasBerries = true;

    public BerryBush() {
        this.type = "berry-bush";
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onSync() {
        this.setMeta("fruit", this.hasBerries);
    }

    @Override
    public void onTick() {
//        this.setMeta("fruit",this.hasBerries);
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return this.hasBerries;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Pick Berries";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            if (this.hasBerries) {
                int amt = RandomUtil.getRandomNumberBetween(3, 8);

                connection.playLocalSoundAtLocation(GameSounds.FORAGE_TREE, this.location, .75f, 1);

                InventoryStack stack = new InventoryStack(new Blueberry(), amt);

                connection.getPlayer().inventory.add(stack);
                connection.displayItemAdded(stack);

                DedicatedServer.instance.getWorld().updateEntity(this);

                this.hasBerries = false;
            }
        }
    }
}
