package com.gamefocal.rivenworld.game.items.resources.econ;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;

public class GoldCoin extends InventoryItem {

    public GoldCoin() {
        this.icon = InventoryDataRow.Coins;
        this.mesh = InventoryDataRow.Coins;
        this.name = "Gold Coin";
        this.desc = "A coin made of gold used for trading";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }
}
