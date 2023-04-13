package com.gamefocal.rivenworld.game.items.resources.minerals.refined;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.recipes.minerals.CopperIgnotRecipe;

public class CopperIgnot extends InventoryItem implements InventoryCraftingInterface {

    public CopperIgnot() {
        this.icon = InventoryDataRow.Copper_Ingot;
        this.mesh = InventoryDataRow.Copper_Ingot;
        this.name = "Copper Bar";
        this.desc = "Perfect for making weapons and things from copper";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new CopperIgnotRecipe();
    }
}
