package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassStairBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldStairBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.GoldStairsBlockRecipe;

public class GoldStairBlockItem extends PlaceableInventoryItem<GoldStairBlockItem> implements InventoryCraftingInterface {

    public GoldStairBlockItem() {
        this.name = "Gold Stairs";
        this.desc = "A stair made of Gold";
        this.icon = InventoryDataRow.Gold_Stairs;
        this.mesh = InventoryDataRow.Gold_Stairs;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("goldstairs");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldStairBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GoldStairsBlockRecipe();
    }
}
