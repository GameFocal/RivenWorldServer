package com.gamefocal.island.game.inventory;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;

public class InventoryStack implements Serializable {

    private String hash = "";

    private InventoryItem item;

    private int amount = 0;

    public InventoryStack(InventoryItem item) {
        this.item = item;
        this.amount = 1;
        this.hash = item.hash();
    }

    public InventoryStack(InventoryItem item, int amt) {
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

    public String getHash() {
        return hash;
    }
}
