package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayStairBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Stone.StoneStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.ClayStairsBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneStairsBlockRecipe;

public class ClayStairBlockItem extends PlaceableInventoryItem<ClayStairBlockItem> implements InventoryCraftingInterface {

    public ClayStairBlockItem() {
        this.name = "Clay Stairs";
        this.desc = "A stair made of clay";
        this.icon = InventoryDataRow.Clay_Stairs;
        this.mesh = InventoryDataRow.Clay_Stairs;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("claystairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ClayStairsBlockRecipe();
    }
}
