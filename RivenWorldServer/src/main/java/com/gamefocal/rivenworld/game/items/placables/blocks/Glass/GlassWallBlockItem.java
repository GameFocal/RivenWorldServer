package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Glass.GlassTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Glass.GlassWallBlockRecipe;

public class GlassWallBlockItem extends PlaceableInventoryItem<GlassWallBlockItem> implements InventoryCraftingInterface {

    public GlassWallBlockItem() {
        this.name = "Glass Wall Block";
        this.desc = "A Wall made of Glass";
        this.icon = InventoryDataRow.Glass_Wall;
        this.mesh = InventoryDataRow.Glass_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glasswallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GlassWallBlockRecipe();
    }
}
