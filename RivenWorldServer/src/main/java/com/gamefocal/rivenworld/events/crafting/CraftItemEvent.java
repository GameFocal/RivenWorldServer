package com.gamefocal.rivenworld.events.crafting;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingJob;
import com.gamefocal.rivenworld.game.ui.CraftingUI;

public class CraftItemEvent extends Event<CraftItemEvent> {

    private HiveNetConnection connection;

    private CraftingRecipe recipe;

    private CraftingJob job;

    private CraftingUI ui;

    public CraftItemEvent(HiveNetConnection connection, CraftingRecipe recipe, CraftingJob job, CraftingUI ui) {
        this.connection = connection;
        this.recipe = recipe;
        this.job = job;
        this.ui = ui;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public CraftingRecipe getRecipe() {
        return recipe;
    }

    public CraftingJob getJob() {
        return job;
    }

    public CraftingUI getUi() {
        return ui;
    }
}
