package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperTileBlockRecipe;

public class CopperTileBlockItem extends PlaceableInventoryItem<CopperTileBlockItem> implements InventoryCraftingInterface {

    public CopperTileBlockItem() {
        this.name = "Copper Tile Block";
        this.desc = "A Tile made of Copper";
        this.icon = InventoryDataRow.Copper_Tile;
        this.mesh = InventoryDataRow.Copper_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("coppertileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperTileBlockRecipe();
    }
}
