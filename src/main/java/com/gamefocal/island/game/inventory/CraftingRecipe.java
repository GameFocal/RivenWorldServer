package com.gamefocal.island.game.inventory;

import com.gamefocal.island.entites.net.HiveNetConnection;

import java.util.Hashtable;
import java.util.LinkedList;

public abstract class CraftingRecipe {

    private Hashtable<Class<? extends InventoryItem>, Integer> requires = new Hashtable<>();

    private Class<? extends InventoryItem> produces = null;

    private int producesAmt = 0;

    private LinkedList<Class<? extends InventoryItem>> requireTools = new LinkedList<>();

    protected float timeToProduceInSeconds = 5;

    public abstract void config();

    public void craft(HiveNetConnection connection, int amt) {

    }

    public void requires(Class<? extends InventoryItem> item, int amt) {
        if (this.requires.containsKey(item)) {
            this.requires.put(item, this.requires.get(item) + amt);
        } else {
            this.requires.put(item, amt);
        }
    }

    public void setProduces(Class<? extends InventoryItem> item, int amt) {
        this.produces = item;
        this.producesAmt = amt;
    }

    public void requireTool(Class<? extends InventoryItem> tool) {
        this.requireTools.add(tool);
    }

    public void setProductionTime(float timeInSeconds) {
        this.timeToProduceInSeconds = timeInSeconds;
    }

}
