package com.gamefocal.rivenworld.game.ui.claim;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.InventoryUtil;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.gamefocal.rivenworld.service.InventoryService;
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
