package com.gamefocal.rivenworld.game.items.placables.blocks.Iron;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldBattlementCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Iron.IronBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class IronBattlementCornerBlockItem extends PlaceableInventoryItem<IronBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public IronBattlementCornerBlockItem() {
        this.name = "Iron Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.IronBattlementCorner;
        this.mesh = InventoryDataRow.IronBattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("ironcastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new IronBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
