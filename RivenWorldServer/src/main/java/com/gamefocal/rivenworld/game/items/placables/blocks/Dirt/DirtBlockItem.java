package com.gamefocal.rivenworld.game.items.placables.blocks.Dirt;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Dirt.DirtBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class DirtBlockItem extends PlaceableInventoryItem<DirtBlockItem> {

    public DirtBlockItem() {
        this.name = "Dirt Block";
        this.desc = "A block of dirt";
        this.icon = InventoryDataRow.Dirt_Block;
        this.mesh = InventoryDataRow.Dirt_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("dirtblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new DirtBlock();
    }
}
