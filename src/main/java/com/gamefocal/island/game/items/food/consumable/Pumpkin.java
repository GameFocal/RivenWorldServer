package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class Pumpkin extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Pumpkin";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}
