package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.Wood1_4CircleBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodTileBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.Wood1_4CircleBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodTileBlockRecipe;

public class WoodTileBlockItem extends PlaceableInventoryItem<WoodTileBlockItem> implements InventoryCraftingInterface {

    public WoodTileBlockItem() {
        this.name = "Wooden Tile Block";
        this.desc = "A Tile made of wood";
        this.icon = InventoryDataRow.Wood_Tile;
        this.mesh = InventoryDataRow.Wood_Tile;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("woodtilecircleblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodTileBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodTileBlockRecipe();
    }
}
