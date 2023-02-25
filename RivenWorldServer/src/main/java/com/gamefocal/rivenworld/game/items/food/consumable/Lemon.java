package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Lemon extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Lemon";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 1f;
    }
}
