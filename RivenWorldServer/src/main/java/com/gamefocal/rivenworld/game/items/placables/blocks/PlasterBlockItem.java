package com.gamefocal.rivenworld.game.items.placables.blocks;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.PlasterBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.PlasterBlockRecipe;

public class PlasterBlockItem extends PlaceableInventoryItem<PlasterBlockItem> implements InventoryCraftingInterface {

    public PlasterBlockItem() {
        this.name = "Plaster Block";
        this.desc = "A block of plaster";
        this.icon = InventoryDataRow.Plaster_Block;
        this.mesh = InventoryDataRow.Plaster_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("plasterblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new PlasterBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new PlasterBlockRecipe();
    }
}
