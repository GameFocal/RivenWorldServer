package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Pineapple extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Pineapple";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}