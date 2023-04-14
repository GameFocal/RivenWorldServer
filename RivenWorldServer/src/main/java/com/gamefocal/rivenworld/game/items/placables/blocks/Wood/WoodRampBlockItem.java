package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.WoodRampBlockRecipe;

public class WoodRampBlockItem extends PlaceableInventoryItem<WoodRampBlockItem> implements InventoryCraftingInterface {

    public WoodRampBlockItem() {
        this.name = "Wooden Ramp";
        this.desc = "A ramp made of wood";
        this.icon = InventoryDataRow.WoodRamp_Block;
        this.mesh = InventoryDataRow.WoodRamp_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("woodramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodRampBlockRecipe();
    }
}
