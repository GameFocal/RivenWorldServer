package com.gamefocal.rivenworld.events.inv;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.game.inventory.Inventory;

public class InventoryMoveEvent extends Event<InventoryMoveEvent> {

    private Inventory from;

    private Inventory to;

    private int fromSlot;

    private int toSlot;

    private int amt;

    public InventoryMoveEvent(Inventory from, Inventory to, int fromSlot, int toSlot, int amt) {
        this.from = from;
        this.to = to;
        this.fromSlot = fromSlot;
        this.toSlot = toSlot;
        this.amt = amt;
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
