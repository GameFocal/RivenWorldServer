package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Clay.ClayBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtBattlementCornerBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;
import com.gamefocal.rivenworld.game.recipes.blocks.Dirt.DirtBattlementCornerBlockRecipe;

public class DirtBattlementCornerBlockItem extends PlaceableInventoryItem<DirtBattlementCornerBlockItem> implements InventoryCraftingInterface {

    public DirtBattlementCornerBlockItem() {
        this.name = "Dirt Corner Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Dirt_BattlementCorner;
        this.mesh = InventoryDataRow.Dirt_BattlementCorner;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirtcastlecorner");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtBattlementCornerBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new DirtBattlementCornerBlockRecipe();
    }
}
