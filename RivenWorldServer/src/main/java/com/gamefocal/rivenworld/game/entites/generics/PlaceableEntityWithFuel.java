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
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.util.InventoryUtil;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.TickUtil;
import com.gamefocal.rivenworld.service.InventoryService;

import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class PlaceableEntityWithFuel<T> extends PlaceableEntity<T> implements EntityStorageInterface, TickEntity, CraftingStation {

    protected transient HashMap<Class<? extends InventoryItem>, Float> fuelSources = new HashMap<>();

    protected Inventory inventory = new Inventory(InventoryType.CAMPFIRE, "Campfire", "campfire", 6);

    protected Inventory fuel = new Inventory(6);

    protected float fuelAmt = 0.00f;

    protected boolean isOn = false;

    protected transient LinkedList<HiveNetConnection> inUseBy = new LinkedList<>();

    public PlaceableEntityWithFuel(String inventoryName, int slots) {
        this.inventory = new Inventory(InventoryType.CAMPFIRE, inventoryName, "campfire", slots, slots);
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

    public float timeInFuel() {
        float time = 0.00f;

        for (Map.Entry<Class<? extends InventoryItem>, Float> e : this.fuelSources.entrySet()) {
            int amt = this.inventory.amtOfType(e.getKey());
            time += (amt * e.getValue());
        }

        return time;
    }

    public List<Class<? extends InventoryItem>> fuelInHighestOrder() {
        ArrayList<Class<? extends InventoryItem>> l = new ArrayList<>();
        l.addAll(this.fuelSources.keySet());
        l.sort(new Comparator<Class<? extends InventoryItem>>() {
            @Override
            public int compare(Class<? extends InventoryItem> o1, Class<? extends InventoryItem> o2) {
                float amt1 = fuelSources.get(o1);
                float amt2 = fuelSources.get(o2);

                if (amt1 > amt2) {
                    return +1;
                } else if (amt1 < amt2) {
                    return -1;
                }

                return 0;
            }
        });
        return l;
    }

    public void consumeFuel() {
        if (this.fuelAmt <= 0f) {
            for (Class<? extends InventoryItem> c : this.fuelInHighestOrder()) {
                if (this.fuel.hasOfType(c)) {
                    this.fuelAmt += TickUtil.SECONDS((long) Math.floor(this.fuelSources.get(c)));
                    this.fuel.removeOfType(c, 1);
                    break;
                }
            }
            this.inventory.updateUIs();
        }
    }

    public String timeLeftInFuel() {
        // In Seconds

        float time = this.timeInFuel();

        float seconds = time;
        float mins = time / 60;
        float hours = mins / 60;
        float days = hours / 24;

        if (days >= 1) {
            return (int) Math.floor(days) + "d";
        } else if (hours >= 1) {
            return (int) Math.floor(hours) + "h";
        } else if (mins >= 1) {
            return (int) Math.floor(mins) + "m";
        } else {
            return (int) Math.floor(seconds) + "s";
        }
    }

    @Override
    public void onSync() {
        if (this.inventory != null) {
            this.setMeta("invid", this.inventory.getUuid().toString());
//            this.setMeta("inv", Base64.getEncoder().encodeToString(InventoryUtil.inventoryToJson(this.inventory).toString().getBytes(StandardCharsets.UTF_8)));
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
            this.fuelAmt--;
            this.consumeFuel();
            if (this.fuelAmt <= 0) {
                this.isOn = false;
            } else {
                // Run the tick
                if (this.inUseBy.size() > 0) {
                    if (this.inventory.getCraftingQueue().tick(this.inUseBy.getFirst())) {
                        this.inventory.updateUIs();
                    }
                } else {
                    this.inventory.getCraftingQueue().tick(null);
                }
            }
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
        return this.isOn;
    }

    @Override
    public boolean hasFuel() {
        return true;
    }

    @Override
    public void toggleOnOff(HiveNetConnection connection) {
        this.isOn = !this.isOn;
        this.inventory.updateUIs();
        DedicatedServer.instance.getWorld().updateEntity(this);
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void onUse(HiveNetConnection connection) {
        this.inUseBy.add(connection);
    }

    @Override
    public void onLeave(HiveNetConnection connection) {
        this.inUseBy.clear();
    }

}
