package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class Tomato extends ConsumableInventoryItem {
    @Override
    public void onConsume(HiveNetConnection connection) {

    }

    @Override
    public String slug() {
        return "Tomato";
    }
}
