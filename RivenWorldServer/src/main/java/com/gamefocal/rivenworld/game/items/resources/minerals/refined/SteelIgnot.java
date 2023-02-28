package com.gamefocal.rivenworld.game.items.resources.minerals.refined;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryIcon;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryMesh;
import com.gamefocal.rivenworld.game.recipes.Minerals.SteelIgnotRecipe;

public class SteelIgnot extends InventoryItem implements InventoryCraftingInterface {
    @Override
    public String slug() {
        return "Steel_Ingot";
    }

    @Override
    public InventoryIcon icon() {
        return null;
    }

    @Override
    public InventoryMesh mesh() {
        return null;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new SteelIgnotRecipe();
    }
}
