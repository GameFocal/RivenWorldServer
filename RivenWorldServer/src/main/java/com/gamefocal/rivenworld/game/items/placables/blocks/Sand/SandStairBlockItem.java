package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogStairBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Sand.SandStairsBlockRecipe;

public class SandStairBlockItem extends PlaceableInventoryItem<SandStairBlockItem> implements InventoryCraftingInterface {

    public SandStairBlockItem() {
        this.name = "Sand Stairs";
        this.desc = "A stair made of Sand";
        this.icon = InventoryDataRow.Sand_Stairs;
        this.mesh = InventoryDataRow.Sand_Stairs;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SandStairsBlockRecipe();
    }
}
