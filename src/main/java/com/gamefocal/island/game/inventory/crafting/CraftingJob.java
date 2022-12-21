package com.gamefocal.island.game.inventory.crafting;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.inventory.CraftingRecipe;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.island.service.TaskService;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CraftingJob implements Serializable {

    private Hashtable<Class<? extends InventoryItem>, Integer> resources = new Hashtable<>();

    private UUID uuid;

    private Inventory sourceInventory;

    private Inventory destinationInventory;

    private CraftingRecipe recipe;

    private long startedAt = 0L;

    private int leftToProduce = 0;

    private InventoryStack output;

    public CraftingJob(Inventory sourceInventory, Inventory destinationInventory, CraftingRecipe recipe, int amt) {
        this.uuid = UUID.randomUUID();
        this.sourceInventory = sourceInventory;
        this.destinationInventory = destinationInventory;
        this.recipe = recipe;
        this.leftToProduce = amt;
        this.output = new InventoryStack(recipe.getProduces(), 0);

        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : this.recipe.getRequires().entrySet()) {
            this.resources.put(e.getKey(), e.getValue() * amt);
        }
    }

    public void start() {
        this.startedAt = System.currentTimeMillis();
//        this.removeFromSource();
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
            float finish = this.startedAt + (this.recipe.getTimeToProduceInSeconds() * 1000);
            float diff = finish - System.currentTimeMillis();

            float percent = diff / (this.recipe.getTimeToProduceInSeconds() * 1000);
            if (percent > 1.0) {
                return 1.0f;
            }

            return diff;
        }

        return 0.0f;
    }

    public void tick(HiveNetConnection connection) {
        if (this.startedAt > 0L) {
            // Has started.
            if (this.isComplete()) {
                // Has completed an item.
                this.removeFromSource();
                this.destinationInventory.add(this.recipe.getProduces(), this.recipe.getProducesAmt());
                this.output.add(this.recipe.getProducesAmt());
                this.leftToProduce--;
                if (this.leftToProduce > 0) {
                    // Still has some left
                    this.startedAt = 0L;
                }

                TaskService.scheduledDelayTask(() -> {
                    connection.updateInventory(destinationInventory);
                }, 5L, false);
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
        for (Map.Entry<Class<? extends InventoryItem>, Integer> e : this.resources.entrySet()) {
            if (e.getValue() > 0) {
                try {
                    this.sourceInventory.add(e.getKey().newInstance(), e.getValue());
                } catch (InstantiationException | IllegalAccessException instantiationException) {
                    instantiationException.printStackTrace();
                }
            }
        }
    }
}
