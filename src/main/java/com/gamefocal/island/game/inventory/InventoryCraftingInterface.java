package com.gamefocal.island.game.inventory;

import com.gamefocal.island.entites.net.HiveNetConnection;

import java.util.LinkedList;

public interface InventoryCraftingInterface {
    public CraftingRecipe canCraft(HiveNetConnection connection);
}
