package com.gamefocal.rivenworld.game.entites.stations;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.PlaceableCraftingEntityWithFuel;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodBlockItem;
import com.gamefocal.rivenworld.game.items.placables.blocks.Wood.WoodHalfBlockItem;
import com.gamefocal.rivenworld.game.items.resources.minerals.raw.Coal;
import com.gamefocal.rivenworld.game.items.resources.misc.Oil;
import com.gamefocal.rivenworld.game.items.resources.misc.Thatch;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodStick;
import com.gamefocal.rivenworld.game.recipes.placables.Anvil_R;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.jail_3_Recipe;
import com.gamefocal.rivenworld.game.recipes.weapons.*;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class FurnacePlaceableCrafting extends PlaceableCraftingEntityWithFuel<FurnacePlaceableCrafting> {

    public FurnacePlaceableCrafting() {
        super("Furnace", 6);

        this.inventory.setType(InventoryType.FURNACE);
        this.inventory.setHasOnOff(true);

        this.type = "FurnacePlaceable";
        this.fuelSources.put(WoodBlockItem.class, 60f);
        this.fuelSources.put(WoodHalfBlockItem.class, 30f);
        this.fuelSources.put(WoodLog.class, 10f);
        this.fuelSources.put(WoodStick.class, 5f);
        this.fuelSources.put(Thatch.class, 2f);
        this.fuelSources.put(Coal.class, 120f);
        this.fuelSources.put(Oil.class, 90f);
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        if (this.inUseBy != null) {
            if (!DedicatedServer.playerIsOnline(this.inUseBy.getUuid())) {
                this.inUseBy = null;
            }
        }

        if (this.inUseBy != null) {
            return "In use by someone";
        } else {
            return "[e] Use";
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 50, 100);
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (this.inUseBy == null) {
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
                new Anvil_R(),
                new SpadeRecipe(),
                new IronHatchetRecipe(),
                new IronPickaxeRecipe(),
                new IronPickaxeRecipe(),
                new IronSwordRecipe(),
                new IronLongSwordRecipe(),
                new IronSpearRecipe(),
                new SteelHatchetRecipe(),
                new SteelPickaxeRecipe(),
                new SteelSwordRecipe(),
                new SteelLongSwordRecipe()
        );
    }
}
