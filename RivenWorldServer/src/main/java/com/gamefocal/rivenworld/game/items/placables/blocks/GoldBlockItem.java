package com.gamefocal.rivenworld.game.items.placables.blocks;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.GoldBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.GoldBlockRecipe;

public class GoldBlockItem extends PlaceableInventoryItem<GoldBlockItem> implements InventoryCraftingInterface {

    public GoldBlockItem() {
        this.name = "Gold Block";
        this.desc = "A block of gold";
        this.icon = InventoryDataRow.Gold_Block;
        this.mesh = InventoryDataRow.Gold_Block;
        this.placable.IsBuildingBlock = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GoldBlockRecipe();
    }
}
