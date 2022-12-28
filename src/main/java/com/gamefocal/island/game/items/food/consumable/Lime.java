package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class Lime extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Lime";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}
