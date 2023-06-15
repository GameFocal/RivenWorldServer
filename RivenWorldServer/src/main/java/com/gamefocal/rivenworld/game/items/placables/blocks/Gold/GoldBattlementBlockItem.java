package com.gamefocal.rivenworld.game.items.placables.blocks.Gold;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Glass.GlassBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Gold.GoldBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Gold.GoldBattlementBlockRecipe;

public class GoldBattlementBlockItem extends PlaceableInventoryItem<GoldBattlementBlockItem> implements InventoryCraftingInterface {

    public GoldBattlementBlockItem() {
        this.name = "Gold Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.GoldBattlement;
        this.mesh = InventoryDataRow.GoldBattlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("goldcastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new GoldBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new GoldBattlementBlockRecipe();
    }
}
