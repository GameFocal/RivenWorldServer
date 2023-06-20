package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;

public abstract class AmmoInventoryItem extends InventoryItem {

    public AmmoInventoryItem() {
//        this.isEquipable = true;
//        this.equipTo = EquipmentSlot.AMMO;

        this.attr(ChatColor.SMALL + "" + ChatColor.ITALIC + "Damage: " + this.damage());
    }

    public abstract float damage();

}
