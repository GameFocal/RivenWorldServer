package com.gamefocal.rivenworld.game.items.placables.blocks.Wood;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Blocks.WoodBattlementCornerBlockRecipe;

public class WoodBattlementCornerBlockItem extends PlaceableInventoryItem<WoodBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public WoodBattlementCornerBlockItem() {
        this.name = "Wooden Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.WoodBattlementCorner_Block;
        this.mesh = InventoryDataRow.WoodBattlementCorner_Block;
        this.placable.IsBuildingBlock = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new WoodBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodBattlementCornerBlockRecipe();
    }
}
