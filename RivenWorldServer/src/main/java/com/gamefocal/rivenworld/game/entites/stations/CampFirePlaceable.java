package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.PlaceableEntityWithFuel;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;

public class CampFirePlaceable extends PlaceableEntityWithFuel<CampFirePlaceable> {

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
        }
    }
}
