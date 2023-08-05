package com.gamefocal.rivenworld.game.farming;

import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.food.consumable.*;
import com.gamefocal.rivenworld.game.items.food.seeds.*;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;

import java.util.concurrent.TimeUnit;

public enum CropType {

    // Tier 1 Food
    CARROT(TimeUnit.HOURS.toMinutes(2)),
    CUCUMBER(TimeUnit.HOURS.toMinutes(2)),
    ONION(TimeUnit.HOURS.toMinutes(2)),
    WHEAT(TimeUnit.HOURS.toMinutes(2), new InventoryStack(new Thatch(), 4), new InventoryStack(new WheatSeed(), 1)),


    // Tier 2 FoodB
    CORN(TimeUnit.HOURS.toMinutes(4), new InventoryStack(new Corn(), 4), new InventoryStack(new CornSeed(), 1)),
    CABBAGE(TimeUnit.HOURS.toMinutes(4), new InventoryStack(new Cabbage(), 1)),
    POTATO(TimeUnit.HOURS.toMinutes(4), new InventoryStack(new Potato(), 2)),
    TOMATO(TimeUnit.HOURS.toMinutes(4), new InventoryStack(new Tomato(), 6), new InventoryStack(new TomatoSeed(), 2)),


    // Tier 3 Food
    PUMPKIN(TimeUnit.HOURS.toMinutes(8), new InventoryStack(new Pumpkin(), 1), new InventoryStack(new PumpkinSeed(), 4)),
    WATERMELON(TimeUnit.HOURS.toMinutes(8), new InventoryStack(new Watermelon(), 1), new InventoryStack(new WatermelonSeed(), 5)),
    ;

    private long timeInMinutes;

    private InventoryStack[] yield = new InventoryStack[0];

    CropType(long timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    CropType(long timeInMinutes, InventoryStack... stacks) {
        this.timeInMinutes = timeInMinutes;
        this.yield = stacks;
    }

    public long getTimeInMinutes() {
        return timeInMinutes;
    }

    public InventoryStack[] getYield() {
        return yield;
    }
}
