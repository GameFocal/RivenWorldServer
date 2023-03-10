package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.PlaceableCraftingEntityWithFuel;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Coal;
import com.gamefocal.rivenworld.game.items.resources.misc.Charcoal;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.recipes.Weapons.*;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;

public class FurnacePlaceableCrafting extends PlaceableCraftingEntityWithFuel<FurnacePlaceableCrafting> {

    public FurnacePlaceableCrafting() {
        super("Furnace", 6);

        this.inventory.setType(InventoryType.FURNACE);
        this.inventory.setHasOnOff(true);

        this.type = "FurnacePlaceable";
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
                new IronHatchetRecipe(),
                new IronPickaxeRecipe(),
                new IronPickaxeRecipe(),
                new IronSwordRecipe(),
                new SteelHatchetRecipe(),
                new SteelPickaxeRecipe(),
                new SteelSwordRecipe()
        );
    }
}
