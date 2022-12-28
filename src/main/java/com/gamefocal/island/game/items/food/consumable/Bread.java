package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class Bread extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Bread";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {
        
    }
}
