package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.entites.generics.PlaceableEntityWithFuel;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;

public class CampFirePlaceable extends PlaceableEntityWithFuel<CampFirePlaceable> implements CraftingStation {

    public CampFirePlaceable() {
        super("Campfire", 6);
        this.type = "CampfirePlaceable";
        this.fuelSources.put(WoodBlockItem.class, 60f);
        this.fuelSources.put(WoodLog.class, 10f);
        this.fuelSources.put(WoodStick.class, 5f);
        this.fuelSources.put(Thatch.class, 2f);
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Use";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);
        if (action == InteractAction.TOGGLE_ON_OFF) {
            // Toggle the entity on and off
            this.isOn = !this.isOn;

            DedicatedServer.instance.getWorld().updateEntity(this);
        } else if (action == InteractAction.USE) {
//            CraftingInventoryUI ui = new CraftingInventoryUI();
//            ui.open(connection, this.inventory);

            RivenCraftingUI ui = new RivenCraftingUI(true);
            ui.open(connection, this);

        }
    }

    @Override
    public Inventory dest() {
        return this.inventory;
    }

    @Override
    public Inventory fuel() {
        return this.fuel;
    }

    @Override
    public CraftingQueue queue() {
        return this.inventory.getCraftingQueue();
    }

    @Override
    public boolean isOn() {
        return false;
    }

    @Override
    public boolean hasFuel() {
        return true;
    }

    @Override
    public void toggleOnOff(HiveNetConnection connection) {

    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void onUse(HiveNetConnection connection) {

    }

    @Override
    public void onLeave(HiveNetConnection connection) {

    }
}
