package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Lime extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Lime";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}
