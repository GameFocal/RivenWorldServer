package com.gamefocal.rivenworld.game.items.placables.blocks.Plaster;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Plaster.PlasterTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Plaster.PlasterWallBlockRecipe;

public class PlasterWallBlockItem extends PlaceableInventoryItem<PlasterWallBlockItem> implements InventoryCraftingInterface {

    public PlasterWallBlockItem() {
        this.name = "Plaster Wall Block";
        this.desc = "A Wall made of Plaster";
        this.icon = InventoryDataRow.Plaster_Wall;
        this.mesh = InventoryDataRow.Plaster_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("plasterwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new PlasterWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new PlasterWallBlockRecipe();
    }
}
