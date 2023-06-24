package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayStairBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.ClayStairsBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperStairsBlockRecipe;

public class CopperStairBlockItem extends PlaceableInventoryItem<CopperStairBlockItem> implements InventoryCraftingInterface {

    public CopperStairBlockItem() {
        this.name = "Copper Stairs";
        this.desc = "A stair made of copper";
        this.icon = InventoryDataRow.Copper_Stairs;
        this.mesh = InventoryDataRow.Copper_Stairs;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("copperstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperStairsBlockRecipe();
    }
}
