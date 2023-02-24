package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Tomato extends ConsumableInventoryItem {
    @Override
    public void onConsume(HiveNetConnection connection) {

    }

    @Override
    public String slug() {
        return "Tomato";
    }
}
