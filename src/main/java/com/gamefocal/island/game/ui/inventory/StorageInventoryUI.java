package com.gamefocal.island.game.ui.inventory;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.ui.GameUI;
import com.gamefocal.island.game.util.InventoryUtil;
import com.google.gson.JsonObject;

public class StorageInventoryUI extends GameUI<Inventory> {
    @Override
    public String name() {
        return "storage-inv";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Inventory obj) {

        JsonObject pl = InventoryUtil.inventoryToJson(connection.getPlayer().inventory);
        JsonObject s = InventoryUtil.inventoryToJson(obj);

        JsonObject a = new JsonObject();
        a.add("pinv", pl);
        a.add("sinv", s);

        return a;
    }

    @Override
    public void onOpen(HiveNetConnection connection, Inventory object) {
        if (object.isOwner(connection)) {
            // Is the owner
            try {
                connection.openInventory(object, true);
            } catch (InventoryOwnedAlreadyException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClose(HiveNetConnection connection, Inventory object) {
        connection.closeInventory(object);
    }
}
