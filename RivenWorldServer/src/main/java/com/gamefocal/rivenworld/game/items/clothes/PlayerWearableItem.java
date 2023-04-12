package com.gamefocal.rivenworld.game.items.clothes;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.generics.EquipmentItem;

public abstract class PlayerWearableItem extends InventoryItem implements InventoryCraftingInterface, EquipmentItem {

    public PlayerWearableItem() {
        this.hasDurability = true;
        this.isEquipable = true;

        /*
         * Setup Attr
         * */
        this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Max Durability: " + this.maxDurability);
        this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Protection: " + this.defend());
    }

    public abstract float defend();

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
