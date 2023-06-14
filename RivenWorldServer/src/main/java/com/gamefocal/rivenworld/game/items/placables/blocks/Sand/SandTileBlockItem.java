package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class SandTileBlockItem extends PlaceableInventoryItem<SandTileBlockItem> implements InventoryCraftingInterface {

    public SandTileBlockItem() {
        this.name = "Sand Tile Block";
        this.desc = "A Tile made of Sand";
        this.icon = InventoryDataRow.Sand_Tile;
        this.mesh = InventoryDataRow.Sand_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandtileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
