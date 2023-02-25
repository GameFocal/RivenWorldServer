package com.gamefocal.rivenworld.game.items.placables.blocks.StoneBrick;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.StoneBrick.StoneBrickBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.StoneBrickBattlementCornerBlockRecipe;

public class StoneBrickBattlementCornerBlockItem extends PlaceableInventoryItem<StoneBrickBattlementCornerBlockItem> implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "StoneBrickBattlementCorner_Block";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new StoneBrickBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new StoneBrickBattlementCornerBlockRecipe();
    }
}
