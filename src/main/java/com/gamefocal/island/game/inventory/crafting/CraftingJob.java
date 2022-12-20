package com.gamefocal.island.game.inventory.crafting;

import com.gamefocal.island.game.inventory.CraftingRecipe;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.inventory.InventoryStack;

import java.io.Serializable;
import java.util.UUID;

public class CraftingJob implements Serializable {

    private UUID uuid;

    private Inventory destinationInventory;

    private CraftingRecipe recipe;

    private float startedAt = 0L;

    private int leftToProduce = 0;

    private InventoryStack output;

    public CraftingJob(Inventory destinationInventory, CraftingRecipe recipe, int amt) {
        this.uuid = UUID.randomUUID();
        this.destinationInventory = destinationInventory;
        this.recipe = recipe;
        this.leftToProduce = amt;
        this.output = new InventoryStack(recipe.getProduces(), 0);
    }

    public void start() {
        this.startedAt = System.currentTimeMillis();
    }

    public CraftingRecipe getRecipe() {
        return recipe;
    }

    public float getStartedAt() {
        return startedAt;
    }

    public boolean isComplete() {
        if (this.startedAt > 0) {
            float finish = this.startedAt + (this.recipe.getTimeToProduceInSeconds() * 1000);

            if (System.currentTimeMillis() >= finish) {
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

    public void tick() {
        if (this.startedAt > 0L) {
            // Has started.

            if (this.isComplete()) {
                // Has completed an item.
                this.destinationInventory.add(this.recipe.getProduces(), this.recipe.getProducesAmt());
                this.output.add(this.recipe.getProducesAmt());
                this.leftToProduce--;
                if (this.leftToProduce > 0) {
                    // Still has some left
                    this.startedAt = 0L;
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
}
