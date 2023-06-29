package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayRampBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperRampBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperRampBlockRecipe;

public class CopperRampBlockItem extends PlaceableInventoryItem<CopperRampBlockItem> implements InventoryCraftingInterface {

    public CopperRampBlockItem() {
        this.name = "Copper Ramp";
        this.desc = "A ramp made of Copper";
        this.icon = InventoryDataRow.Copper_Ramp;
        this.mesh = InventoryDataRow.Copper_Ramp;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("copperramp");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperRampBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperRampBlockRecipe();
    }
}
