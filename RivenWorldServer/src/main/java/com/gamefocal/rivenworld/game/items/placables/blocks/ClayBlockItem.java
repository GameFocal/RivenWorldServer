package com.gamefocal.rivenworld.game.items.placables.blocks;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.ClayBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.ClayBlockRecipe;

public class ClayBlockItem extends PlaceableInventoryItem<ClayBlockItem> implements InventoryCraftingInterface {

    public ClayBlockItem() {
        this.name = "Clay Block";
        this.desc = "A block of clay";
        this.icon = InventoryDataRow.Clay_Block;
        this.mesh = InventoryDataRow.Clay_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("clayblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ClayBlockRecipe();
    }
}
