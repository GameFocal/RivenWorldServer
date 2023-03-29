package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.WoodBlockRecipe;

public class WoodBlockItem extends PlaceableInventoryItem<WoodBlockItem> implements InventoryCraftingInterface {

    public WoodBlockItem() {
        this.name = "Wooden Block";
        this.desc = "A block made of wood";
        this.icon = InventoryDataRow.Wood_Block;
        this.mesh = InventoryDataRow.Wood_Block;
        this.placable.IsBuildingBlock = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodBlockRecipe();
    }
}
