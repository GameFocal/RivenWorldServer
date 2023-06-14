package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodBattlementBlockRecipe;

public class ClayBattlementBlockItem extends PlaceableInventoryItem<ClayBattlementBlockItem> implements InventoryCraftingInterface {

    public ClayBattlementBlockItem() {
        this.name = "Clay Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.ClayBattlement;
        this.mesh = InventoryDataRow.ClayBattlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("claycastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
