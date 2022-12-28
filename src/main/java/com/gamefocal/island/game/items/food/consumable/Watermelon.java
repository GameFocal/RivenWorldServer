package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class Watermelon extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Watermelon";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}
