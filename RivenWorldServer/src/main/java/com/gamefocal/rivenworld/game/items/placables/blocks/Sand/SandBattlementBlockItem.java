package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Log.LogBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Sand.SandBattlementBlockRecipe;

public class SandBattlementBlockItem extends PlaceableInventoryItem<SandBattlementBlockItem> implements InventoryCraftingInterface {

    public SandBattlementBlockItem() {
        this.name = "Sand Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Sand_Battlement;
        this.mesh = InventoryDataRow.Sand_Battlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandcastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SandBattlementBlockRecipe();
    }
}
