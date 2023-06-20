package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.ClayTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.ClayWallBlockRecipe;

public class ClayWallBlockItem extends PlaceableInventoryItem<ClayWallBlockItem> implements InventoryCraftingInterface {

    public ClayWallBlockItem() {
        this.name = "Clay Wall Block";
        this.desc = "A Wall made of Clay";
        this.icon = InventoryDataRow.Clay_Wall;
        this.mesh = InventoryDataRow.Clay_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("claywallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ClayWallBlockRecipe();
    }
}
