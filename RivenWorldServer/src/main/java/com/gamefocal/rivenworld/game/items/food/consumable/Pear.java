package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Pear extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Pear";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}
