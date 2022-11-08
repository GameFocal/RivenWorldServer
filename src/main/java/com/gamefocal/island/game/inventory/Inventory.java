package com.gamefocal.island.game.inventory;

import java.io.Serializable;

public class Inventory implements Serializable {

    private int storageSpace = 16;

    private int maxStack = 64;

    private InventoryStack[] items = new InventoryStack[0];

    public Inventory(int storageSpace) {
        this.storageSpace = storageSpace;
        this.items = new InventoryStack[this.storageSpace];
    }

    public Inventory(InventoryStack[] items) {
        this.items = items;
        this.storageSpace = this.items.length;
    }

    public void add(InventoryStack stack) {
        InventoryStack currentStack = null;
        for (InventoryStack s : this.items) {
            if (s.getType() == stack.getType() && (s.getCount() + stack.getCount()) <= maxStack) {
                currentStack = s;
                break;
            }
        }

        if (currentStack == null) {
            // None found add a new stack
            for (int i = 0; i < this.items.length; i++) {
                if (this.items[i] == null) {
                    this.items[i] = stack;
                }
            }
        } else {
            currentStack.setCount(currentStack.getCount() + stack.getCount());
        }
    }

    public InventoryStack get(int index) {
        return this.items[index];
    }

    public void clear(int index) {
        this.items[index] = null;
    }

    public void updateCount(int index, int amt) {
        this.items[index].setCount(amt);
        if (this.items[index].getCount() == 0) {
            this.clear(index);
        }
    }

    public void addToSlot(int index, int amt) {
        this.items[index].setCount(this.items[index].getCount() + amt);
        if (this.items[index].getCount() > 64) {
            this.items[index].setCount(64);
        }
    }

    public void set(int index, InventoryStack stack) {
        this.items[index] = stack;
    }

}
