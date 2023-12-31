package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldStairBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.IronStairsBlockRecipe;

public class IronStairBlockItem extends PlaceableInventoryItem<IronStairBlockItem> implements InventoryCraftingInterface {

    public IronStairBlockItem() {
        this.name = "Iron Stairs";
        this.desc = "A stair made of Iron";
        this.icon = InventoryDataRow.Iron_Stairs;
        this.mesh = InventoryDataRow.Iron_Stairs;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("ironstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronStairsBlockRecipe();
    }
}
