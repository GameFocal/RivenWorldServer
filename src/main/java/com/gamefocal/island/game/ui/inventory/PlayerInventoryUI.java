package com.gamefocal.island.game.ui.inventory;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.ui.GameUI;
import com.gamefocal.island.game.util.InventoryUtil;
import com.google.gson.JsonObject;

public class PlayerInventoryUI extends GameUI<Inventory> {
    @Override
    public String name() {
        return "player-inv";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Inventory obj) {
        return InventoryUtil.inventoryToJson(obj);
    }

    @Override
    public void onOpen(HiveNetConnection connection, Inventory object) {
        try {

            object.setLinkedUI(this);

            connection.openInventory(object,true);
        } catch (InventoryOwnedAlreadyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(HiveNetConnection connection, Inventory object) {
        connection.closeInventory(object);
    }

    @Override
    public void onAction(InteractAction action, String tag) {

    }
}
