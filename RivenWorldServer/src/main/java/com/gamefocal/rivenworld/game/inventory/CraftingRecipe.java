package com.gamefocal.rivenworld.game.inventory;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

public abstract class CraftingRecipe implements Serializable {

    protected float timeToProduceInSeconds = 5;
    private Hashtable<Class<? extends InventoryItem>, Integer> requires = new Hashtable<>();
    private InventoryItem produces = null;
    private int producesAmt = 0;
    private LinkedList<Class<? extends InventoryItem>> requireTools = new LinkedList<>();

    public CraftingRecipe() {
        this.config();
    }

    public abstract void config();

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

    public int canMakeAmount(Inventory source) {
        return source.canCraftAmt(this);
    }

    public HashMap<Class<? extends InventoryItem>, Integer> getHas(Inventory source) {
        HashMap<Class<? extends InventoryItem>, Integer> m = new HashMap<>();
        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : this.getRequires().entrySet()) {
            m.put(e.getKey(), source.amtOfType(e.getKey()));
        }
        return m;
    }

    public JsonObject toJson(Inventory fromInventory) {

        JsonObject o = new JsonObject();

        if (InventoryCraftingInterface.class.isAssignableFrom(this.produces.getClass())) {
            // has a crafting interface

            InventoryCraftingInterface craftingInterface = (InventoryCraftingInterface) this.produces;
            o.addProperty("timeToProduce", this.timeToProduceInSeconds);

            JsonArray requires = new JsonArray();
            JsonArray has = new JsonArray();
            for (Map.Entry<Class<? extends InventoryItem>, Integer> m : this.requires.entrySet()) {
                try {
                    InventoryItem i = m.getKey().newInstance();
                    InventoryStack s = new InventoryStack(i, m.getValue());
                    requires.add(s.toJson());

                    InventoryStack hasStack = new InventoryStack(i, fromInventory.amtOfType(m.getKey()));
                    has.add(hasStack.toJson());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            int canMake = fromInventory.canCraftAmt(this);

            o.add("requires", requires);
            o.add("has", has);
            o.add("produces", new InventoryStack(this.produces, this.producesAmt).toJson());
            o.addProperty("hasEnough", canMake > 0);
            o.addProperty("canMakeAmt", canMake);
        }

        return o;
    }

    @Override
    public boolean equals(Object obj) {
        if (CraftingRecipe.class.isAssignableFrom(obj.getClass())) {
            return obj.getClass().getSimpleName().equalsIgnoreCase(this.getClass().getSimpleName());
        }
        return false;
    }
}
