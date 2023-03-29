package com.gamefocal.rivenworld.game.items.resources.minerals.refined;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.recipes.minerals.IronIgnotRecipe;

public class IronIgnot extends InventoryItem implements InventoryCraftingInterface {

    public IronIgnot() {
        this.icon = InventoryDataRow.Iron_Ingot;
        this.mesh = InventoryDataRow.Iron_Ingot;
        this.name = "Iron Bar";
        this.desc = "Perfect for making weapons and things from iron";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new IronIgnotRecipe();
    }
}
