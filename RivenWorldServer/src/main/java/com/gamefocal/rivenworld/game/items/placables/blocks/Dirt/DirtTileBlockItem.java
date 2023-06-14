package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtTileBlockItem extends PlaceableInventoryItem<DirtTileBlockItem> implements InventoryCraftingInterface {

    public DirtTileBlockItem() {
        this.name = "Dirt Tile Block";
        this.desc = "A Tile made of Dirt";
        this.icon = InventoryDataRow.Dirt_Tile;
        this.mesh = InventoryDataRow.Dirt_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirttileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
