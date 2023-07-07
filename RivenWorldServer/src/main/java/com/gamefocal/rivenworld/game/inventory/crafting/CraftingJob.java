package com.gamefocal.rivenworld.game.inventory.crafting;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CraftingJob implements Serializable {

    private Hashtable<Class<? extends InventoryItem>, Integer> resources = new Hashtable<>();

    private transient UUID ownedBy;

    private UUID uuid;

    private transient Inventory sourceInventory;

    private transient Inventory destinationInventory;

    private CraftingRecipe recipe;

    private long startedAt = 0L;

    private int leftToProduce = 0;

    private InventoryStack output;

    private Location location;

    private transient CraftingUI fromUi;

    public CraftingJob(HiveNetConnection connection, CraftingUI fromUi, Inventory sourceInventory, Inventory destinationInventory, CraftingRecipe recipe, int amt, Location location) {
        this.ownedBy = connection.getUuid();
        this.uuid = UUID.randomUUID();
        this.sourceInventory = sourceInventory;
        this.destinationInventory = destinationInventory;
        this.recipe = recipe;
        this.leftToProduce = amt;
        this.output = new InventoryStack(recipe.getProduces(), 0);
        this.location = location;
        this.fromUi = fromUi;

        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : this.recipe.getRequires().entrySet()) {
            this.resources.put(e.getKey(), e.getValue() * amt);
        }
    }

    public UUID getOwnedBy() {
        return ownedBy;
    }

    public void start() {
        this.startedAt = System.currentTimeMillis();
//        this.removeFromSource();
    }

    public void setDestinationInventory(Inventory destinationInventory) {
        this.destinationInventory = destinationInventory;
    }

    public void setSourceInventory(Inventory inventory) {
        this.sourceInventory = inventory;
    }

    public Location getLocation() {
        return location;
    }

    public boolean removeFromSource() {
        for (Map.Entry<Class<? extends InventoryItem>, Integer> m : this.resources.entrySet()) {
            int left = this.resources.get(m.getKey());
            left -= this.recipe.getRequires().get(m.getKey());
            this.resources.put(m.getKey(), left);
        }
        return true;
    }

    public CraftingRecipe getRecipe() {
        return recipe;
    }

    public float getStartedAt() {
        return startedAt;
    }

    public boolean isComplete() {
        if (this.startedAt > 0) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.startedAt) >= this.recipe.getTimeToProduceInSeconds()) {
                return true;
            }
        }

        return false;
    }

    public float percentComplete() {
        if (this.startedAt > 0) {

            float currentSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.startedAt);

            float percent = currentSeconds / this.recipe.getTimeToProduceInSeconds();
            if (percent > 1.0) {
                return 1.0f;
            }

            return percent;
        }

        return 0.0f;
    }

    public void tick(HiveNetConnection connection) {
        if (this.destinationInventory.canAdd(new InventoryStack(this.recipe.getProduces(), this.recipe.getProducesAmt()))) {
            if (this.startedAt > 0L) {
                // Has started.
                if (this.isComplete()) {
                    // Has completed an item.
                    this.removeFromSource();
                    try {
                        this.destinationInventory.add(this.recipe.getProduces().getClass().newInstance(), this.recipe.getProducesAmt());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    this.output.add(this.recipe.getProducesAmt());
                    this.leftToProduce--;
                    if (this.leftToProduce > 0) {
                        // Still has some left
                        this.startedAt = 0L;
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.CRAFTING, this.location, 300, 5f, 1f);
//                    this.sourceInventory.updateUIs(connection);
//                    this.destinationInventory.updateUIs(connection);

                        if (connection != null && connection.getOpenUI() != null) {
                            connection.getOpenUI().update(connection);
                        }
                    }

                    if (connection != null) {
                        connection.updateCraftingUI();
                    }
                } else {
//                connection.updateInventory(this.sourceInventory);
                }
            }
        }
    }

    public InventoryStack previewStack() {
        return new InventoryStack(this.recipe.getProduces(), this.leftToProduce);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Inventory getDestinationInventory() {
        return destinationInventory;
    }

    public int getLeftToProduce() {
        return leftToProduce;
    }

    public InventoryStack getOutput() {
        return output;
    }

    public boolean isStarted() {
        return (this.startedAt > 0L);
    }

    public boolean inProgress() {
        if (this.startedAt > 0) {
            return !this.isComplete();
        }

        return false;
    }

    public void cancel() {
        this.cancel(null);
    }

    public void cancel(Inventory sendBackTo) {
        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : this.resources.entrySet()) {
            if (e.getValue() > 0) {
                try {
                    if (sendBackTo == null) {
                        this.sourceInventory.add(e.getKey().newInstance(), e.getValue());
                    } else {
                        sendBackTo.add(e.getKey().newInstance(), e.getValue());
                    }
                } catch (InstantiationException | IllegalAccessException instantiationException) {
                    instantiationException.printStackTrace();
                }
            }
        }
    }

    public CraftingUI getFromUi() {
        return fromUi;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("isWorking", (this.startedAt > 0));
        o.addProperty("id", this.uuid.toString());
        o.add("item", new InventoryStack(this.recipe.getProduces(), 1).toJson());
        o.addProperty("left", this.leftToProduce);
        o.addProperty("percent", this.percentComplete());
        o.addProperty("time", this.recipe.getTimeToProduceInSeconds());

//        System.out.println(this.percentComplete());

        return o;
    }
}
