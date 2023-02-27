package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.EntityStorageInterface;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.ui.inventory.CraftingInventoryUI;

public class WorkBenchPlaceable extends PlaceableEntity<WorkBenchPlaceable> implements EntityStorageInterface, TickEntity {

    protected Inventory inventory = new Inventory(InventoryType.WORKBENCH, "Workbench", "Workbench", 6, 6);

    public WorkBenchPlaceable() {
        this.type = "workbenchPlaceable";
        this.inventory.setAttachedEntity(this.uuid);
//        this.inventory.setCraftingQueue(new CraftingQueue(6));
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        this.inventory.getCraftingQueue().tick(null);
    }

    @Override
    public void onInventoryUpdated() {

    }

    @Override
    public void onInventoryOpen() {

    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            CraftingInventoryUI ui = new CraftingInventoryUI();
            ui.open(connection, this.inventory);
        }
    }

    @Override
    public void onInventoryClosed() {

    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Use";
    }
}
