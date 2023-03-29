package com.gamefocal.rivenworld.game.items.weapons.Basic;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryItemType;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodenClubRecipe;

public class WoodenClub extends MeleeWeapon implements InventoryCraftingInterface {

    public WoodenClub() {
        this.type = InventoryItemType.PRIMARY;
        this.icon = InventoryDataRow.WoodenClub;
        this.mesh = InventoryDataRow.WoodenClub;
        this.name = "Wooden Club";
        this.desc = "A large wooden club perfect for hitting someone with";
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
