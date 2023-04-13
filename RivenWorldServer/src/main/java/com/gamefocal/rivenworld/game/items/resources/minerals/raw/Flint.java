package com.gamefocal.rivenworld.game.items.resources.minerals.raw;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;

public class Flint extends ToolInventoryItem {

    public Flint() {
        this.icon = InventoryDataRow.Flint;
        this.mesh = InventoryDataRow.Flint;
        this.name = "Sharp Rock";
        this.desc = "A rock that is sharp... could be used to cut rope.";
        this.isEquipable = true;
        this.type = InventoryItemType.PRIMARY;
    }

    @Override
    public float hit() {
        return 1;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
