package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.entites.generics.PlaceableEntityWithFuel;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.recipes.Resources.CleanWaterFromDirtyRecipe;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;

public class CampFirePlaceable extends PlaceableEntityWithFuel<CampFirePlaceable> {

    public CampFirePlaceable() {
        super("Campfire", 1);
        this.type = "CampfirePlaceable";

        this.fuelSources.put(WoodBlockItem.class, 240f);
        this.fuelSources.put(WoodLog.class, 120f);
        this.fuelSources.put(WoodStick.class, 60f);
        this.fuelSources.put(Thatch.class, 30f);
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Use";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);
        if (action == InteractAction.USE) {
            RivenCraftingUI ui = new RivenCraftingUI(true);
            ui.open(connection, this);
        }
    }

    @Override
    public void getRecipes() {
        this.inventory.getCraftingQueue().addAllowedRecipes(
                new CleanWaterFromDirtyRecipe()
        );
    }
}
