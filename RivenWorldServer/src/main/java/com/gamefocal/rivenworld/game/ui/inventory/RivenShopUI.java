package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.econ.Shop;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.FuelEntity;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.service.InventoryService;
import com.google.gson.JsonObject;

public class RivenShopUI extends GameUI<Shop> {

    public RivenShopUI() {
    }

    @Override
    public String name() {
        return "npc-shop";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Shop obj) {
        connection.updatePlayerInventory();

        JsonObject o = new JsonObject();
        o.add("shop", obj.toJson());

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, Shop object) {
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
    }

    @Override
    public void onClose(HiveNetConnection connection, Shop object) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        }
    }

}
