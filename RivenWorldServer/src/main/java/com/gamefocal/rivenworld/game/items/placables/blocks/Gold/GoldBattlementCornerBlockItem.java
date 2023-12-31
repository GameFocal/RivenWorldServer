package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassBattlementCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.GoldBattlementCornerBlockRecipe;

public class GoldBattlementCornerBlockItem extends PlaceableInventoryItem<GoldBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public GoldBattlementCornerBlockItem() {
        this.name = "Gold Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.GoldBattlementCorner;
        this.mesh = InventoryDataRow.GoldBattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("goldcastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GoldBattlementCornerBlockRecipe();
    }
}
