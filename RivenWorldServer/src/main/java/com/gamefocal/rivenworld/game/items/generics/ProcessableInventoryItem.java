package com.gamefocal.rivenworld.game.items.generics;

import com.gamefocal.rivenworld.game.inventory.InventoryStack;

public interface ProcessableInventoryItem {
    InventoryStack processesTo();
    float timeToProcess();
}
