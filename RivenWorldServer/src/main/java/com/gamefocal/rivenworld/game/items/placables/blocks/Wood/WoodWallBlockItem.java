package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodTileBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodWallBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodTileBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodWallBlockRecipe;

public class WoodWallBlockItem extends PlaceableInventoryItem<WoodWallBlockItem> implements InventoryCraftingInterface {

    public WoodWallBlockItem() {
        this.name = "Wooden Wall Block";
        this.desc = "A Wall made of wood";
        this.icon = InventoryDataRow.Wood_Wall;
        this.mesh = InventoryDataRow.Wood_Wall;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("woodwallblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodWallBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodWallBlockRecipe();
    }
}
