package com.gamefocal.island.game.ui.claim;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.ui.GameUI;
import com.gamefocal.island.game.util.InventoryUtil;
import com.gamefocal.island.models.GameLandClaimModel;
import com.gamefocal.island.service.InventoryService;
import com.google.gson.JsonObject;

public class ClaimUI extends GameUI<GameLandClaimModel> {
    @Override
    public String name() {
        return "claim";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, GameLandClaimModel obj) {

        JsonObject plInv = InventoryUtil.inventoryToJson(connection.getPlayer().inventory);
        JsonObject claimData = new JsonObject();


        JsonObject main = new JsonObject();
        main.add("plinv", plInv);
        main.add("objinv", InventoryUtil.inventoryToJson(obj.runeStorage));
        main.add("claim", claimData);
        return main;
    }

    @Override
    public void onOpen(HiveNetConnection connection, GameLandClaimModel object) {
        object.runeStorage.setLinkedUI(this);
        connection.getPlayer().inventory.setLinkedUI(this);

        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        DedicatedServer.get(InventoryService.class).trackInventory(object.runeStorage);
    }

    @Override
    public void onClose(HiveNetConnection connection, GameLandClaimModel object) {

    }

    @Override
    public void onAction(InteractAction action, String tag) {

    }
}
