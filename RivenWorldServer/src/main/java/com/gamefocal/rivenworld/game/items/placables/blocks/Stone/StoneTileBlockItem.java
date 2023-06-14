package com.gamefocal.rivenworld.game.items.placables.blocks.Stone;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class StoneTileBlockItem extends PlaceableInventoryItem<StoneTileBlockItem> implements InventoryCraftingInterface {

    public StoneTileBlockItem() {
        this.name = "Stone Tile Block";
        this.desc = "A Tile made of Stone";
        this.icon = InventoryDataRow.Stone_Tile;
        this.mesh = InventoryDataRow.Stone_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("stonetileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
