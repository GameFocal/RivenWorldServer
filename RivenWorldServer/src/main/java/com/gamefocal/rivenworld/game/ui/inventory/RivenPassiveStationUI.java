package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.PassiveResourceStation;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.service.InventoryService;
import com.google.gson.JsonObject;

public class RivenPassiveStationUI extends GameUI<PassiveResourceStation> {

    protected HiveNetConnection connection;
    protected String name;

    public RivenPassiveStationUI(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return "passive-station";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, PassiveResourceStation obj) {

        connection.updatePlayerInventory();

        JsonObject object = new JsonObject();

        JsonObject passsive = new JsonObject();
        passsive.addProperty("name", this.name);
        passsive.addProperty("title", obj.getProducesName());
        passsive.addProperty("desc", obj.getProducesDesc());
        passsive.addProperty("percent", obj.getAmt() / obj.getMaxAmt());
        passsive.addProperty("amt", obj.getAmt());

        object.add("pass", passsive);

        return object;
    }

    @Override
    public void onOpen(HiveNetConnection connection, PassiveResourceStation object) {
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        connection.getPlayer().inventory.attachToUI(this);
    }

    @Override
    public void onClose(HiveNetConnection connection, PassiveResourceStation object) {
        connection.getPlayer().inventory.detachFromUI(this);
    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("gather")) {
            this.getAttached().onGather(connection, Long.parseLong(data[0]));
        }
    }

}
