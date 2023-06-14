package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Copper.CopperBattlementBlock;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtBattlementBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtBattlementBlockItem extends PlaceableInventoryItem<DirtBattlementBlockItem> implements InventoryCraftingInterface {

    public DirtBattlementBlockItem() {
        this.name = "Dirt Battlement";
        this.desc = "Looks great on top of a wall";
        this.icon = InventoryDataRow.Dirt_Battlement;
        this.mesh = InventoryDataRow.Dirt_Battlement;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirtcastle");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtBattlementBlock();
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new ;
    }
}
