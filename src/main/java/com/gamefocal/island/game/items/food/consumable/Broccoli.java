package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class Broccoli extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Broccoli";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}