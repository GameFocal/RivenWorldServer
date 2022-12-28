package com.gamefocal.island.game.items.food.consumable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.ConsumableInventoryItem;

public class Apple extends ConsumableInventoryItem {
    @Override
    public String slug() {
        return "Apple";
    }

    @Override
    public void onConsume(HiveNetConnection connection) {

    }
}
