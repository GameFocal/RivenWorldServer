package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Iron.IronBattlementBlockRecipe;

public class IronBattlementBlockItem extends PlaceableInventoryItem<IronBattlementBlockItem> implements InventoryCraftingInterface {

    public IronBattlementBlockItem() {
        this.name = "Iron Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.IronBattlement;
        this.mesh = InventoryDataRow.IronBattlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("ironcastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronBattlementBlockRecipe();
    }
}
