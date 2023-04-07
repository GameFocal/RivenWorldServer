package com.gamefocal.rivenworld.events.inv;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryClick;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;

public class InventoryItemClickEvent extends Event<InventoryItemClickEvent> {

    private Inventory inventory;

    private InventoryStack item;

    private int slot;

    private HiveNetConnection by;

    private InventoryClick click;

    public InventoryItemClickEvent(Inventory inventory, InventoryStack item, int slot, HiveNetConnection by, InventoryClick click) {
        this.inventory = inventory;
        this.item = item;
        this.slot = slot;
        this.by = by;
        this.click = click;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public InventoryStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public HiveNetConnection getBy() {
        return by;
    }

    public InventoryClick getClick() {
        return click;
    }
}
