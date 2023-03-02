package com.gamefocal.rivenworld.game.items.weapons.sword;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.recipes.Weapons.SteelSwordRecipe;

public class SteelSword extends MeleeWeapon implements InventoryCraftingInterface {

    public SteelSword() {
        this.icon = InventoryDataRow.Steel_Sword;
        this.mesh = InventoryDataRow.Steel_Sword;
        this.hasDurability = true;
        this.durability = 100f;
        this.name = "Steel Sword";
        this.desc = "A one-handed sword crafted from steel";
        this.data.getAttributes().add("15 Damage");
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
        return new SteelSwordRecipe();
    }
}
