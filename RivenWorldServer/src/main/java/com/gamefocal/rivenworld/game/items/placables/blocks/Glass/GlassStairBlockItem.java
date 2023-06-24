package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtStairBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Glass.GlassStairsBlockRecipe;

public class GlassStairBlockItem extends PlaceableInventoryItem<GlassStairBlockItem> implements InventoryCraftingInterface {

    public GlassStairBlockItem() {
        this.name = "Glass Stairs";
        this.desc = "A stair made of Glass";
        this.icon = InventoryDataRow.Glass_Stairs;
        this.mesh = InventoryDataRow.Glass_Stairs;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glassstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GlassStairsBlockRecipe();
    }
}
