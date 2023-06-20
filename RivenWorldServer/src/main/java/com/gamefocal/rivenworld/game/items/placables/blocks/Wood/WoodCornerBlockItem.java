package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.WoodCornerBlockRecipe;

public class WoodCornerBlockItem extends PlaceableInventoryItem<WoodCornerBlockItem> implements InventoryCraftingInterface {

    public WoodCornerBlockItem() {
        this.name = "Wooden Corner Block";
        this.desc = "A corner block made of wood";
        this.icon = InventoryDataRow.WoodCorner_Block;
        this.mesh = InventoryDataRow.WoodCorner_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("woodcorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodCornerBlockRecipe();
    }
}
