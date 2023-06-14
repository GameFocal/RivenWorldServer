package com.gamefocal.rivenworld.game.items.placables.blocks.Copper;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Copper.CopperBattlementCornerBlockRecipe;

public class CopperBattlementCornerBlockItem extends PlaceableInventoryItem<CopperBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public CopperBattlementCornerBlockItem() {
        this.name = "Copper Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.CopperBattlementCorner;
        this.mesh = InventoryDataRow.CopperBattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("coppercastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new CopperBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperBattlementCornerBlockRecipe();
    }
}
