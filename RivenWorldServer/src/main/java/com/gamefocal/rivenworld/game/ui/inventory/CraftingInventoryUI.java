package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.events.inv.InventoryCloseEvent;
import com.gamefocal.rivenworld.events.inv.InventoryOpenEvent;
import com.gamefocal.rivenworld.game.exceptions.InventoryOwnedAlreadyException;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.InventoryUtil;
import com.gamefocal.rivenworld.service.InventoryService;
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

//        object.setCraftingQueue(new CraftingQueue(6));

        object.setLinkedUI(this);
        connection.getPlayer().inventory.setLinkedUI(this);

        DedicatedServer.get(InventoryService.class).trackInventory(object);
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);

        try {
            object.takeOwnership(connection,true);
            connection.getPlayer().inventory.takeOwnership(connection,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public void onAction(HiveNetConnection connection,InteractAction action, String tag, String[] data) {

    }
}
