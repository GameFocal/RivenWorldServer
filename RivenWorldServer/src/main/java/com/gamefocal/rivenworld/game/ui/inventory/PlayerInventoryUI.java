package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.InventoryUtil;
import com.google.gson.JsonObject;

public class PlayerInventoryUI extends GameUI<Inventory> {
    @Override
    public String name() {
        return "player-inv";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Inventory obj) {
        return new JsonObject();
    }

    @Override
    public void onOpen(HiveNetConnection connection, Inventory object) {
        try {

            object.setLinkedUI(this);

            connection.openInventory(object, true);
        } catch (InventoryOwnedAlreadyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(HiveNetConnection connection, Inventory object) {
        connection.closeInventory(object);
    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {

    }
}
