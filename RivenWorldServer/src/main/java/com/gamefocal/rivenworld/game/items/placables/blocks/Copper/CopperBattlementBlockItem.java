package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperBattlementBlockRecipe;

public class CopperBattlementBlockItem extends PlaceableInventoryItem<CopperBattlementBlockItem> implements InventoryCraftingInterface {

    public CopperBattlementBlockItem() {
        this.name = "Copper Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.CopperBattlement;
        this.mesh = InventoryDataRow.CopperBattlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("coppercastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperBattlementBlockRecipe();
    }
}
