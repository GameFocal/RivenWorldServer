package com.gamefocal.island.game.entites.placable;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.InventoryType;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.service.InventoryService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CampFirePlaceable extends PlaceableEntity<CampFirePlaceable> {

    public Inventory inventory = new Inventory(InventoryType.CRAFTING, "Campfire", "campfire", 6);

    public CampFirePlaceable() {
        this.type = "CampfirePlaceable";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            // Toggle the inventory for the campfire :)

            try {
                connection.openDualInventory(this.inventory, true);
            } catch (InventoryOwnedAlreadyException e) {
                e.printStackTrace();
            }

        }

        super.onInteract(connection, action, inHand);
    }

    @Override
    public void onSpawn() {
        DedicatedServer.get(InventoryService.class).trackInventory(this.inventory);
    }

    @Override
    public void onSync() {
        if (this.inventory != null) {
            this.setMeta("invid", this.inventory.getUuid().toString());
            this.setMeta("inv", Base64.getEncoder().encodeToString(InventoryUtil.inventoryToJson(this.inventory).toString().getBytes(StandardCharsets.UTF_8)));
        }
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }
}
