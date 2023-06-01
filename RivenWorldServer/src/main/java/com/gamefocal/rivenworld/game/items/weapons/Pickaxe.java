package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;

public abstract class Pickaxe extends ToolInventoryItem {

    public Pickaxe() {
        this.isEquipable = true;
        this.type = InventoryItemType.PRIMARY;
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("Can be used to mine ores or in combat");
        super.generateUpperRightHelpText();
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
    }
}
