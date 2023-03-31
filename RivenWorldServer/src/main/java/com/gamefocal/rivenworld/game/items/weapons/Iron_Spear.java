package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.recipes.weapons.IronSpearRecipe;

public class Iron_Spear extends MeleeWeapon implements InventoryCraftingInterface {

    public Iron_Spear() {
        this.icon = InventoryDataRow.Iron_Spear;
        this.mesh = InventoryDataRow.Iron_Spear;
        this.hasDurability = true;
        this.durability = 100f;
        this.name = "Iron  Spear";
        this.desc = "A Spear made out of Iron";
        this.data.getAttributes().add("15 Damage");
        this.type = InventoryItemType.PRIMARY;
        this.tag("weapon", "spear");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 0;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronSpearRecipe();
    }
}
