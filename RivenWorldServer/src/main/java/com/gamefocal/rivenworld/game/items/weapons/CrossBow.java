package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.recipes.weapons.BasicBowRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.CrossBowRecipe;

public class CrossBow extends RangedWeapon implements InventoryCraftingInterface {

    public CrossBow() {
        this.icon = InventoryDataRow.Crossbow;
        this.mesh = InventoryDataRow.Crossbow;
        this.name = "CrossBow";
        this.desc = "A Crossbow made of string and wood and Iron";
        this.spawnNames.add("crossbow");
        this.initDurability(200);
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CrossBowRecipe();
    }

    @Override
    public String toSocket() {
        return "Primary";
    }
}
