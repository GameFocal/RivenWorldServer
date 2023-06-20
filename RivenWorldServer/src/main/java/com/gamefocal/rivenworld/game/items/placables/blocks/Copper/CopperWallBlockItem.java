package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperWallBlockRecipe;

public class CopperWallBlockItem extends PlaceableInventoryItem<CopperWallBlockItem> implements InventoryCraftingInterface {

    public CopperWallBlockItem() {
        this.name = "Copper Wall Block";
        this.desc = "A Wall made of Copper";
        this.icon = InventoryDataRow.Copper_Wall;
        this.mesh = InventoryDataRow.Copper_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("copperwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperWallBlockRecipe();
    }
}
