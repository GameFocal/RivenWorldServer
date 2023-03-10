package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.recipes.Weapons.SmallShieldRecipe;

public class SmallShield extends ToolInventoryItem implements InventoryCraftingInterface {

    public SmallShield() {
        this.icon = InventoryDataRow.Small_Shield;
        this.mesh = InventoryDataRow.Small_Shield;
        this.name = "Small Iron Shield";
        this.desc = "A small shield forged from Iron";
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
        return new SmallShieldRecipe();
    }
}
