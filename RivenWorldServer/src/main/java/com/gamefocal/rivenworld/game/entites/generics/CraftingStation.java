package com.gamefocal.rivenworld.game.entites.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.util.Location;

public interface CraftingStation {

    Inventory dest();

    Inventory fuel();

    CraftingQueue queue();

    boolean isOn();

    boolean hasFuel();

    void toggleOnOff(HiveNetConnection connection);

    Location getLocation();

}
