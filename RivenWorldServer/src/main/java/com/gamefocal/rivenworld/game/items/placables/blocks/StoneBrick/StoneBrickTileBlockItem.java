package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.StoneBrickTileBlockRecipe;

public class StoneBrickTileBlockItem extends PlaceableInventoryItem<StoneBrickTileBlockItem> implements InventoryCraftingInterface {

    public StoneBrickTileBlockItem() {
        this.name = "StoneBrick Tile Block";
        this.desc = "A Tile made of StoneBrick";
        this.icon = InventoryDataRow.StoneBrick_Tile;
        this.mesh = InventoryDataRow.StoneBrick_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonebricktileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickTileBlockRecipe();
    }
}
