package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Blackberry extends ConsumableInventoryItem {
    @Override
    public float onConsume(HiveNetConnection connection) {
        return 2f;
    }

    @Override
    public String slug() {
        return "Blackberry";
    }
}
