package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogBattlementCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Sand.SandBattlementCornerBlockRecipe;

public class SandBattlementCornerBlockItem extends PlaceableInventoryItem<SandBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public SandBattlementCornerBlockItem() {
        this.name = "Sand Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Sand_BattlementCorner;
        this.mesh = InventoryDataRow.Sand_BattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandcastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SandBattlementCornerBlockRecipe();
    }
}
