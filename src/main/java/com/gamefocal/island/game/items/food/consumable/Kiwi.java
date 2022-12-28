package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class Kiwi extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Kiwi";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}
