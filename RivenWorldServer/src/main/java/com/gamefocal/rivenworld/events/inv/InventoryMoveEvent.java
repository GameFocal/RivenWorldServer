package com.gamefocal.rivenworld.events.inv;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;

public class InventoryMoveEvent extends Event<InventoryMoveEvent> {

    private HiveNetConnection connection;

    private Inventory from;

    private Inventory to;

    private int fromSlot;

    private int toSlot;

    private int amt;

    private InventoryStack item;

    public InventoryMoveEvent(HiveNetConnection connection, Inventory from, Inventory to, int fromSlot, int toSlot, int amt) {
        this.from = from;
        this.to = to;
        this.fromSlot = fromSlot;
        this.toSlot = toSlot;
        this.amt = amt;
        this.connection = connection;

        this.item = this.from.get(this.fromSlot);
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public InventoryStack getItem() {
        return item;
    }

    public Inventory getFrom() {
        return from;
    }

    public Inventory getTo() {
        return to;
    }

    public int getFromSlot() {
        return fromSlot;
    }

    public int getToSlot() {
        return toSlot;
    }

    public int getAmt() {
        return amt;
    }
}
