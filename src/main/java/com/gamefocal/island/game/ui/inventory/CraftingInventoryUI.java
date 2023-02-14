package com.gamefocal.island.game.ui.inventory;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.events.inv.InventoryCloseEvent;
import com.gamefocal.island.events.inv.InventoryOpenEvent;
import com.gamefocal.island.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.Inventory;
import com.gamefocal.island.game.ui.GameUI;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.service.InventoryService;
import com.google.gson.JsonObject;

public class CraftingInventoryUI extends GameUI<Inventory> {
    @Override
    public String name() {
        return "crafting-inv";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Inventory obj) {

        JsonObject pl = InventoryUtil.inventoryToJson(connection.getPlayer().inventory);
        JsonObject s = InventoryUtil.inventoryToJson(obj);

        JsonObject a = new JsonObject();
        a.add("pinv", pl);
        a.add("cinv", s);

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
