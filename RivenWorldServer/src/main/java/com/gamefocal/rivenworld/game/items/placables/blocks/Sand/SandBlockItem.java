package com.gamefocal.rivenworld.game.items.placables.blocks.Sand;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.Sand.SandBlock;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.PlaceableInventoryItem;

public class SandBlockItem extends PlaceableInventoryItem<SandBlockItem> {

    public SandBlockItem() {
        this.name = "Sand Block";
        this.desc = "A block of sand";
        this.icon = InventoryDataRow.Sand_Block;
        this.mesh = InventoryDataRow.Sand_Block;
        this.placable.IsBuildingBlock = true;
        this.spawnNames.add("sandblock");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public GameEntity spawnItem() {
        return new SandBlock();
    }
}
