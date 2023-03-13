package com.gamefocal.rivenworld.game.entites.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ui.inventory.RivenFuelUI;
import com.gamefocal.rivenworld.game.util.TickUtil;

import java.util.*;

public class FuelEntity<T> extends PlaceableEntity<T> implements TickEntity {

    protected Inventory fuel = new Inventory(6);
    protected float fuelAmt = 0.00f;
    protected boolean isOn = false;
    protected transient HashMap<Class<? extends InventoryItem>, Float> fuelSources = new HashMap<>();
    protected transient HiveNetConnection inUseBy = null;

    public FuelEntity() {
        this.fuel.setAttachedEntity(this.uuid);
    }

    public Inventory getFuel() {
        return fuel;
    }

    public float getFuelAmt() {
        return fuelAmt;
    }

    public boolean isOn() {
        return isOn;
    }

    public HashMap<Class<? extends InventoryItem>, Float> getFuelSources() {
        return fuelSources;
    }

    public HiveNetConnection getInUseBy() {
        return inUseBy;
    }

    @Override
    public void onSync() {
        this.setMeta("on", (this.isOn) ? "y" : "n");
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    public void toggleOnOff() {
        this.isOn = !this.isOn;
    }

    public void onTick() {
        if (this.isOn) {
            this.fuelAmt--;
            this.consumeFuel();
            if (this.fuelAmt <= 0) {
                this.isOn = false;
            } else {
                // Run the tick
                if (this.inUseBy == null) {
                    if (this.fuel.getCraftingQueue().tick(this.inUseBy)) {
                        this.fuel.updateUIs();
                    }
                } else {
                    this.fuel.getCraftingQueue().tick(null);
                }
            }
        }
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);
        if (action == InteractAction.USE && this.inUseBy == null) {
            // Toggle the inventory for the campfire :)
            RivenFuelUI ui = new RivenFuelUI();
            ui.open(connection, this);
        }
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        if (this.inUseBy != null) {
            return "In use by someone";
        } else {
            return "[e] Use";
        }
    }

    public float timeInFuel() {
        float time = 0.00f;

        for (Map.Entry<Class<? extends InventoryItem>, Float> e : this.fuelSources.entrySet()) {
            int amt = this.fuel.amtOfType(e.getKey());
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
                    return -1;
                } else if (amt1 < amt2) {
                    return +1;
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
            this.fuel.updateUIs();
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

}
