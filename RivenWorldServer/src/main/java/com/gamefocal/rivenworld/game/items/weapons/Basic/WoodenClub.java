package com.gamefocal.rivenworld.game.items.weapons.Basic;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.recipes.Weapons.WoodenClubRecipe;

public class WoodenClub extends MeleeWeapon implements InventoryCraftingInterface {

    public WoodenClub() {
        this.icon = InventoryDataRow.WoodenClub;
        this.mesh = InventoryDataRow.WoodenClub;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 2;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new WoodenClubRecipe();
    }
}
