package com.gamefocal.rivenworld.game.items.placables.blocks.Glass;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Glass.GlassTileBlockRecipe;

public class GlassTileBlockItem extends PlaceableInventoryItem<GlassTileBlockItem> implements InventoryCraftingInterface {

    public GlassTileBlockItem() {
        this.name = "Glass Tile Block";
        this.desc = "A Tile made of Glass";
        this.icon = InventoryDataRow.Glass_Tile;
        this.mesh = InventoryDataRow.Glass_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("glasstileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GlassTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GlassTileBlockRecipe();
    }
}
