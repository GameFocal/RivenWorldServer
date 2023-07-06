package com.gamefocal.rivenworld.game.items.ammo;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.AmmoInventoryItem;
import com.gamefocal.rivenworld.game.recipes.weapons.ammo.SteelArrow_R;

public class SteelArrow extends AmmoInventoryItem implements InventoryCraftingInterface {

    public SteelArrow() {
        this.name = "Steel Arrow";
        this.desc = "A stick with a sharp end that can kill something";
        this.icon = InventoryDataRow.Steel_Arrow;
        this.mesh = InventoryDataRow.Steel_Arrow;
    }

    @Override
    public float damage() {
        return 10;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SteelArrow_R();
    }
}
