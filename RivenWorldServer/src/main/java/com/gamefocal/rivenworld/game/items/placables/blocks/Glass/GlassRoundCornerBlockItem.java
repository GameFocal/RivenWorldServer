package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtRoundCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassRoundCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Glass.GlassRoundCornerBlockRecipe;

public class GlassRoundCornerBlockItem extends PlaceableInventoryItem<GlassRoundCornerBlockItem> implements InventoryCraftingInterface {

    public GlassRoundCornerBlockItem() {
        this.name = "Glass Round corner Block";
        this.desc = "A block made of Glass";
        this.icon = InventoryDataRow.Glass_RoundCorner;
        this.mesh = InventoryDataRow.Glass_RoundCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glassroundcornerblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassRoundCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GlassRoundCornerBlockRecipe();
    }
}
