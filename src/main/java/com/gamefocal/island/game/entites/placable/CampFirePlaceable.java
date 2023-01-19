package com.gamefocal.island.game.entites.placable;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.entites.blocks.WoodBlock;
import com.gamefocal.island.game.entites.placable.generics.PlaceableEntityWithFuel;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.InventoryType;
import com.gamefocal.island.game.items.placables.blocks.WoodBlockItem;
import com.gamefocal.island.game.items.resources.wood.WoodLog;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.service.InventoryService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CampFirePlaceable extends PlaceableEntityWithFuel<CampFirePlaceable> {

    public CampFirePlaceable() {
        super("Campfire",6);
        this.type = "CampfirePlaceable";
        this.fuelSources.put(WoodBlockItem.class,60f);
        this.fuelSources.put(WoodLog.class,10f);
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);
        if(action == InteractAction.TOGGLE_ON_OFF) {
            // Toggle the entity on and off
            this.isOn = !this.isOn;
        }
    }
}
