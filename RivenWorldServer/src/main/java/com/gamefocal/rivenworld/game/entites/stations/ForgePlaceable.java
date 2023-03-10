package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.PlaceableEntityWithFuel;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Coal;
import com.gamefocal.rivenworld.game.items.resources.misc.Charcoal;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.recipes.Blocks.GoldBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.Blocks.IronBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.Minerals.GoldIgnotRecipe;
import com.gamefocal.rivenworld.game.recipes.Minerals.IronIgnotRecipe;
import com.gamefocal.rivenworld.game.recipes.Minerals.SteelIgnotRecipe;
import com.gamefocal.rivenworld.game.recipes.Resources.CleanWaterFromDirtyRecipe;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;

public class ForgePlaceable extends PlaceableEntityWithFuel<ForgePlaceable> {

    public ForgePlaceable() {
        super("Forge", 6);

        this.inventory.setType(InventoryType.FORGE);
        this.inventory.setHasOnOff(true);

        this.type = "ForgePlaceable";
        this.fuelSources.put(WoodBlockItem.class, 60f);
        this.fuelSources.put(WoodLog.class, 10f);
        this.fuelSources.put(WoodStick.class, 5f);
        this.fuelSources.put(Thatch.class, 2f);
        this.fuelSources.put(Coal.class,120f);
        this.fuelSources.put(Charcoal.class,90f);
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        if (this.inUseBy.size() > 0) {
            return "In use by someone";
        } else {
            return "[e] Use";
        }
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (this.inUseBy.size() == 0) {
            super.onInteract(connection, action, inHand);
            if (action == InteractAction.USE) {
                RivenCraftingUI ui = new RivenCraftingUI(true);
                ui.open(connection, this);
            }
        }
    }

    @Override
    public void getRecipes() {
        this.inventory.getCraftingQueue().addAllowedRecipes(
                new IronIgnotRecipe(),
                new GoldIgnotRecipe(),
                new SteelIgnotRecipe(),
                new IronBlockRecipe(),
                new GoldBlockRecipe()
        );
    }
}
