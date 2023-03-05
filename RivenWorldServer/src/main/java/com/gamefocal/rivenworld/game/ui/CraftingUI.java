package com.gamefocal.rivenworld.game.ui;

import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.ArrayList;

public interface CraftingUI {

    public CraftingQueue queue();

    public Inventory getSource();

    public Inventory getDest();

    public Location getLocation();

}
