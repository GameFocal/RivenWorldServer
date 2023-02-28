package com.gamefocal.rivenworld.game.inventory;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.UUID;

public class InventoryStack implements Serializable, Cloneable {

    private String hash = "";

    private InventoryItem item;

    private int amount = 0;

    public InventoryStack() {
    }

    public InventoryStack(InventoryItem item) {
        item.itemUUID = UUID.randomUUID();
        this.item = item;
        this.amount = 1;
        this.hash = item.hash();
    }

    public InventoryStack(InventoryItem item, int amt) {
        item.itemUUID = UUID.randomUUID();
        this.item = item;
        this.amount = amt;
        this.hash = item.hash();
    }

    public InventoryItem getItem() {
        return item;
    }

    public void setItem(InventoryItem item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void add(int amt) {
        this.amount += amt;
    }

    public void remove(int amt) {
        this.amount -= amt;
    }

    public void clear() {
        this.amount = 0;
    }

    public String getHash() {
        return hash;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.add("item", this.item.toJson());
        o.addProperty("amt", this.amount);
        return o;
    }
}
