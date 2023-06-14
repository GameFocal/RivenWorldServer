package com.gamefocal.rivenworld.game.items.placables.blocks.Clay;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayBattlementCornerBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Wood.WoodBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.WoodBattlementCornerBlockRecipe;

public class ClayBattlementCornerBlockItem extends PlaceableInventoryItem<ClayBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public ClayBattlementCornerBlockItem() {
        this.name = "Clay Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.ClayBattlementCorner;
        this.mesh = InventoryDataRow.ClayBattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("claycastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new ClayBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ();
    }
}
