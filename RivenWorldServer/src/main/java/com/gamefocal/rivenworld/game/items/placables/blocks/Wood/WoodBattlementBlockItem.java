package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodBattlementBlockRecipe;

public class WoodBattlementBlockItem extends PlaceableInventoryItem<WoodBattlementBlockItem> implements InventoryCraftingInterface {

    public WoodBattlementBlockItem() {
        this.name = "Wooden Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.WoodBattlement_Block;
        this.mesh = InventoryDataRow.WoodBattlement_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("woodcastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodBattlementBlockRecipe();
    }
}
