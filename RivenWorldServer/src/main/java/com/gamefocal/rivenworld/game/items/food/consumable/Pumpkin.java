package com.gamefocal.rivenworld.game.items.food.consumable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.items.generics.ConsumableInventoryItem;

public class Pumpkin extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Pumpkin";
    }

    @Override
    public float onConsume(HiveNetConnection connection) {
        return 15f;
    }
}
