package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.ClayTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodTileBlockRecipe;

public class ClayTileBlockItem extends PlaceableInventoryItem<ClayTileBlockItem> implements InventoryCraftingInterface {

    public ClayTileBlockItem() {
        this.name = "Clay Tile Block";
        this.desc = "A Tile made of Clay";
        this.icon = InventoryDataRow.Clay_Tile;
        this.mesh = InventoryDataRow.Clay_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("claytileblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ClayTileBlockRecipe();
    }
}
