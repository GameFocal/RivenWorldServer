package com.gamefocal.rivenworld.game.items.placables.blocks.Plaster;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Plaster.PlasterCornerBlockRecipe;

public class PlasterCornerBlockItem extends PlaceableInventoryItem<PlasterCornerBlockItem> implements InventoryCraftingInterface {

    public PlasterCornerBlockItem() {
        this.name = "Plaster Corner Block";
        this.desc = "A corner block made of Plaster";
        this.icon = InventoryDataRow.Plaster_CornerBlock;
        this.mesh = InventoryDataRow.Plaster_CornerBlock;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("plastercorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new PlasterCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new PlasterCornerBlockRecipe();
    }
}
