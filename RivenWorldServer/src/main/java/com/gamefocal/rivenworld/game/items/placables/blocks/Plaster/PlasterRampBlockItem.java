package com.gamefocal.rivenworld.game.items.placables.blocks.Plaster;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Plaster.PlasterRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Plaster.PlasterRampBlockRecipe;

public class PlasterRampBlockItem extends PlaceableInventoryItem<PlasterRampBlockItem> implements InventoryCraftingInterface {

    public PlasterRampBlockItem() {
        this.name = "Plaster Ramp";
        this.desc = "A ramp made of Plaster";
        this.icon = InventoryDataRow.Plaster_Ramp;
        this.mesh = InventoryDataRow.Plaster_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("plasterramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new PlasterRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new PlasterRampBlockRecipe();
    }
}
