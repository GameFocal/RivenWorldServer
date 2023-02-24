package com.gamefocal.rivenworld.game.ui.claim;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
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

    private Inventory fuel = new Inventory(1);

    @Override
    public JsonObject data(HiveNetConnection connection, GameLandClaimModel obj) {

        JsonObject plInv = InventoryUtil.inventoryToJson(connection.getPlayer().inventory);
        JsonObject claimData = new JsonObject();


        JsonObject main = new JsonObject();
        main.add("plinv", plInv);
        main.add("objinv", InventoryUtil.inventoryToJson(this.fuel));
        main.add("claim", claimData);
//        main.add("fuel", InventoryUtil.inventoryToJson(obj.runeStorage));
        return main;
    }

    @Override
    public void onOpen(HiveNetConnection connection, GameLandClaimModel object) {
        this.fuel.setLocked(false);
        this.fuel.setLinkedUI(this);
        this.fuel.setName("Claim Fuel");

//        object.runeStorage.setLinkedUI(this);
        connection.getPlayer().inventory.setLinkedUI(this);

        try {
            this.fuel.takeOwnership(connection,true);
            connection.getPlayer().inventory.takeOwnership(connection,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
//        DedicatedServer.get(InventoryService.class).trackInventory(object.runeStorage);
        DedicatedServer.get(InventoryService.class).trackInventory(this.fuel);
    }

    @Override
    public void onClose(HiveNetConnection connection, GameLandClaimModel object) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {

    }
}
