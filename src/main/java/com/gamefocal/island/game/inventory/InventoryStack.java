package com.gamefocal.island.game.inventory;

import java.io.Serializable;

public class InventoryStack implements Serializable {

    private Class<? extends InventoryItem> type;

    private int count = 1;

    public InventoryStack(Class<? extends InventoryItem> type) {
        this.type = type;
        this.count = 1;
    }

    public InventoryStack(Class<? extends InventoryItem> type, int count) {
        this.type = type;
        this.count = count;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class<? extends InventoryItem> type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
