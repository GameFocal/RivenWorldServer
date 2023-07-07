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
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.*;
import com.gamefocal.rivenworld.game.recipes.blocks.Glass.*;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.*;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.*;
import com.gamefocal.rivenworld.game.recipes.minerals.CopperIgnotRecipe;
import com.gamefocal.rivenworld.game.recipes.minerals.GoldIgnotRecipe;
import com.gamefocal.rivenworld.game.recipes.minerals.IronIgnotRecipe;
import com.gamefocal.rivenworld.game.recipes.minerals.SteelIgnotRecipe;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class ForgePlaceableCrafting extends PlaceableCraftingEntityWithFuel<ForgePlaceableCrafting> {

    public ForgePlaceableCrafting() {
        super("Forge", 6);

        this.inventory.setType(InventoryType.FORGE);
        this.inventory.setHasOnOff(true);

        this.type = "ForgePlaceable";
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
        return ShapeUtil.makeBoundBox(this.location.toVector(), 50, 150);
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
                //Ingot
                new IronIgnotRecipe(),
                new CopperIgnotRecipe(),
                new GoldIgnotRecipe(),
                new SteelIgnotRecipe(),

                //Iron
                new IronBlockRecipe(),
                new IronHalfBlockRecipe(),
                new IronCornerBlockRecipe(),
                new IronBattlementBlockRecipe(),
                new IronBattlementCornerBlockRecipe(),
                new IronStairsBlockRecipe(),
                new IronRampBlockRecipe(),
                new IronTileBlockRecipe(),
                new IronWallBlockRecipe(),
                new IronRoundCornerBlockRecipe(),
                new Iron1_4CircleBlockRecipe(),


                //Copper
                new CopperBlockRecipe(),
                new CopperHalfBlockRecipe(),
                new CopperCornerBlockRecipe(),
                new CopperBattlementBlockRecipe(),
                new CopperBattlementCornerBlockRecipe(),
                new CopperStairsBlockRecipe(),
                new CopperRampBlockRecipe(),
                new CopperTileBlockRecipe(),
                new CopperWallBlockRecipe(),
                new CopperRoundCornerBlockRecipe(),
                new Copper1_4CircleBlockRecipe(),

                //Gold
                new GoldBlockRecipe(),
                new GoldHalfBlockRecipe(),
                new GoldCornerBlockRecipe(),
                new GoldBattlementBlockRecipe(),
                new GoldBattlementCornerBlockRecipe(),
                new GoldStairsBlockRecipe(),
                new GoldRampBlockRecipe(),
                new GoldTileBlockRecipe(),
                new GoldWallBlockRecipe(),
                new GoldRoundCornerBlockRecipe(),
                new Gold1_4CircleBlockRecipe(),

                //Glass
                new GlassBlockRecipe(),
                new GlassHalfBlockRecipe(),
                new GlassCornerBlockRecipe(),
                new GlassBattlementBlockRecipe(),
                new GlassBattlementCornerBlockRecipe(),
                new GlassStairsBlockRecipe(),
                new GlassRampBlockRecipe(),
                new GlassTileBlockRecipe(),
                new GlassWallBlockRecipe(),
                new GlassRoundCornerBlockRecipe(),
                new Glass1_4CircleBlockRecipe()
        );
    }
}
