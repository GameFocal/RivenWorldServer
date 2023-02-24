package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.inv.InventoryCloseEvent;
import com.gamefocal.rivenworld.events.inv.InventoryOpenEvent;
import com.gamefocal.rivenworld.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.InventoryUtil;
import com.gamefocal.rivenworld.service.InventoryService;
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

        object.setLinkedUI(this);
        connection.getPlayer().inventory.setLinkedUI(this);

        DedicatedServer.get(InventoryService.class).trackInventory(object);
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);

        try {
            connection.openInventory(object, true);
        } catch (InventoryOwnedAlreadyException e) {
            e.printStackTrace();
        }

        new InventoryOpenEvent(object,connection).call();
    }

    @Override
    public void onClose(HiveNetConnection connection, Inventory object) {

        object.setLinkedUI(null);
        connection.getPlayer().inventory.setLinkedUI(null);

        connection.closeInventory(object);
        object.releaseOwnership();

        new InventoryCloseEvent(object, connection).call();
    }

    @Override
    public void onAction(InteractAction action, String tag) {

    }
}