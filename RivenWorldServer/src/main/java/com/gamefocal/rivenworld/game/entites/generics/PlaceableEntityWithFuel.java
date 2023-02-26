package com.gamefocal.rivenworld.game.entites.generics;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.util.InventoryUtil;
import com.gamefocal.rivenworld.service.InventoryService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public abstract class PlaceableEntityWithFuel<T> extends PlaceableEntity<T> implements EntityStorageInterface, TickEntity {

    protected transient HashMap<Class<? extends InventoryItem>, Float> fuelSources = new HashMap<>();

    protected Inventory inventory = new Inventory(InventoryType.CAMPFIRE, "Campfire", "campfire", 6);

    protected boolean isOn = false;

    public PlaceableEntityWithFuel(String inventoryName, int slots) {
        this.inventory = new Inventory(InventoryType.CAMPFIRE, inventoryName, "campfire", slots,6);
        this.inventory.setHasOnOff(true);
        this.inventory.setAttachedEntity(this.uuid);
        this.inventory.setName("Campfire");
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);
        if (action == InteractAction.USE && this.inventory != null) {
            // Toggle the inventory for the campfire :)

            try {
                connection.openDualInventory(this.inventory, true);
            } catch (InventoryOwnedAlreadyException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onInventoryUpdated() {
        DedicatedServer.instance.getWorld().updateEntity(this);
    }

    @Override
    public void onInventoryOpen() {

    }

    @Override
    public void onInventoryClosed() {
        DedicatedServer.instance.getWorld().updateEntity(this);
    }

    public String timeLeftInFuel() {

        float time = 0.00f;

        for (Map.Entry<Class<? extends InventoryItem>, Float> e : this.fuelSources.entrySet()) {
            int amt = this.inventory.amtOfType(e.getKey());
            time += (amt * e.getValue());
        }

        // In Seconds

        float seconds = time;
        float mins = time / 60;
        float hours = mins / 60;
        float days = hours / 24;

        if(days >= 1) {
            return (int) Math.floor(days) + "d";
        } else if(hours >= 1) {
            return (int) Math.floor(hours) + "h";
        } else if(mins >= 1) {
            return (int) Math.floor(mins) + "m";
        } else {
            return (int) Math.floor(seconds) + "s";
        }
    }

    @Override
    public void onSync() {
        if (this.inventory != null) {
            this.setMeta("invid", this.inventory.getUuid().toString());
            this.setMeta("inv", Base64.getEncoder().encodeToString(InventoryUtil.inventoryToJson(this.inventory).toString().getBytes(StandardCharsets.UTF_8)));
            this.setMeta("on", (this.isOn) ? "y" : "n");
            this.setMeta("time", this.timeLeftInFuel()); // TODO: Calc this based on items.
        }
    }

    @Override
    public void onSpawn() {
        this.inventory.setAttachedEntity(this.uuid);
        DedicatedServer.get(InventoryService.class).trackInventory(this.inventory);
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        if (this.isOn) {
            this.inventory.getCraftingQueue().tick(null);
        }
    }

}
