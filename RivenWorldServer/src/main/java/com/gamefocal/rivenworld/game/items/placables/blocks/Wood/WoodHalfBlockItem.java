package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodHalfBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.WoodHalfBlockRecipe;

public class WoodHalfBlockItem extends PlaceableInventoryItem<WoodHalfBlockItem> implements InventoryCraftingInterface {

    public WoodHalfBlockItem() {
        this.name = "Wooden Half Block";
        this.desc = "A half block made of wood";
        this.icon = InventoryDataRow.WoodHalf_Block;
        this.mesh = InventoryDataRow.WoodHalf_Block;
        this.placable.IsBuildingBlock = true;
        this.placable.HalfBlock = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodHalfBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodHalfBlockRecipe();
    }
}
