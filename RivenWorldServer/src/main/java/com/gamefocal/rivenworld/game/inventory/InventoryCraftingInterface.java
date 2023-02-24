package com.gamefocal.rivenworld.game.inventory;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;

public interface InventoryCraftingInterface {
    public CraftingRecipe canCraft(HiveNetConnection connection);
}
