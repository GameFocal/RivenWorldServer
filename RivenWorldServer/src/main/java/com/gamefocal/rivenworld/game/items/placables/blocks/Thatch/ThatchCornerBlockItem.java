package com.gamefocal.rivenworld.game.items.placables.blocks.Thatch;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Thatch.ThatchCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.ThatchCornerBlockRecipe;

public class ThatchCornerBlockItem extends PlaceableInventoryItem<ThatchCornerBlockItem> implements InventoryCraftingInterface {

    public ThatchCornerBlockItem() {
        this.name = "Thatch Corner Block";
        this.desc = "A corner block made of thatch";
        this.icon = InventoryDataRow.ThatchCorner_Block;
        this.mesh = InventoryDataRow.ThatchCorner_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("thatchcorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ThatchCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ThatchCornerBlockRecipe();
    }
}
