package com.gamefocal.island.game.inventory;

import com.gamefocal.island.entites.net.HiveNetConnection;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedList;

public abstract class CraftingRecipe implements Serializable {

    private Hashtable<Class<? extends InventoryItem>, Integer> requires = new Hashtable<>();

    private InventoryItem produces = null;

    private int producesAmt = 0;

    private LinkedList<Class<? extends InventoryItem>> requireTools = new LinkedList<>();

    protected float timeToProduceInSeconds = 5;

    public abstract void config();

    public CraftingRecipe() {
        this.config();
    }

    public void craft(HiveNetConnection connection, int amt) {

    }

    public void requires(Class<? extends InventoryItem> item, int amt) {
        if (this.requires.containsKey(item)) {
            this.requires.put(item, this.requires.get(item) + amt);
        } else {
            this.requires.put(item, amt);
        }
    }

    public void setProduces(InventoryItem item, int amt) {
        this.produces = item;
        this.producesAmt = amt;
    }

    public void requireTool(Class<? extends InventoryItem> tool) {
        this.requireTools.add(tool);
    }

    public void setProductionTime(float timeInSeconds) {
        this.timeToProduceInSeconds = timeInSeconds;
    }

    public Hashtable<Class<? extends InventoryItem>, Integer> getRequires() {
        return requires;
    }

    public InventoryItem getProduces() {
        return produces;
    }

    public int getProducesAmt() {
        return producesAmt;
    }

    public LinkedList<Class<? extends InventoryItem>> getRequireTools() {
        return requireTools;
    }

    public float getTimeToProduceInSeconds() {
        return timeToProduceInSeconds;
    }
}
