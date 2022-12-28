package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class PurpleGrape extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Purple_Grapes";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}
